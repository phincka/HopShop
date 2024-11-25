package pl.hincka.hopshop.data.repository

import android.util.Log
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.presentation.dashboard.ListsState
import pl.hincka.hopshop.presentation.dashboard.RemoveSharedListState
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import pl.hincka.hopshop.presentation.list.RemoveListState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class ListRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) : ListRepository {
    override suspend fun getLists(): ListsState = suspendCoroutine { continuation ->
        val lists = mutableListOf<ListModel>()
        var itemsCount = ItemsCountModel(
            items = 0,
            selected = 0
        )

        firebaseAuth.currentUser?.let { currentUser ->
            firebaseFireStore
                .collection("lists")
                .where(
                    Filter.or(
                        Filter.equalTo("ownerId", currentUser.uid),
                        Filter.arrayContains("sharedIds", currentUser.email)
                    )
                )
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val listData = document.data


                        listData?.let { data ->
                            val sharedIds = data["sharedIds"] as? List<String> ?: listOf("")

                            val list = ListModel(
                                id = document.id,
                                ownerId = data["ownerId"] as? String ?: "",
                                name = data["name"] as? String ?: "",
                                description = data["description"] as? String ?: "",
                                tag = data["tag"] as? String ?: "",
                                sharedIds = sharedIds,
                                isShared = currentUser.email in sharedIds,
                            )

                            lists.add(list)
                        }
                    }

                    continuation.resume(
                        ListsState.Success(
                            lists = lists,
                            itemsCount = itemsCount
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        } ?: continuation.resume(
            ListsState.Success(
                lists = lists,
                itemsCount = itemsCount
            )
        )
    }

    override suspend fun getListById(listId: String): ListModel? =
        suspendCoroutine { continuation ->

            firebaseAuth.currentUser?.let { currentUser ->
                firebaseFireStore
                    .collection("lists")
                    .document(listId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val hiveData = documentSnapshot.data

                        hiveData?.let { data ->
                            val sharedIds = data["sharedIds"] as? List<String> ?: listOf("")

                            val list = ListModel(
                                id = documentSnapshot.id,
                                ownerId = data["ownerId"] as? String ?: "",
                                name = data["name"] as? String ?: "",
                                description = data["description"] as? String ?: "",
                                tag = data["tag"] as? String ?: "",
                                sharedIds = sharedIds,
                                isShared = currentUser.uid in sharedIds,
                            )

                            continuation.resume(list)
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } ?: continuation.resume(null)
        }

    override suspend fun getListItemsCount(listId: String): ItemsCountModel =
        suspendCoroutine { continuation ->
            var itemsCount = ItemsCountModel(
                items = 0,
                selected = 0
            )

            firebaseAuth.currentUser?.let { currentUser ->
                firebaseFireStore
                    .collection("items")
                    .whereEqualTo("listId", listId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        itemsCount = itemsCount.copy(items = querySnapshot.size())

                        for (document in querySnapshot.documents) {
                            val listData = document.data

                            listData?.let { data ->
                                if (data["selected"] as Boolean) {
                                    itemsCount = itemsCount.copy(selected = itemsCount.selected + 1)
                                }
                            }
                        }


                        Log.d("LOG_H", itemsCount.toString())

                        continuation.resume(itemsCount)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } ?: continuation.resume(itemsCount)
        }

    override suspend fun createList(formListModel: FormListModel): CreateListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val docRef = firebaseFireStore
                        .collection("lists")
                        .document()
                    val id = docRef.id

                    val list = ListModel(
                        id = id,
                        name = formListModel.name,
                        ownerId = currentUser.uid,
                        description = formListModel.description,
                        tag = formListModel.tag,
                        sharedIds = formListModel.sharedIds,
                        isShared = formListModel.sharedIds.isNotEmpty(),
                    )

                    docRef.set(list)

                    continuation.resume(CreateListState.Redirect(listId = id))
                }
            } else {
                continuation.resume(CreateListState.Error("hive_state_no_user"))
            }
        }

    override suspend fun editList(
        listId: String,
        name: String,
        tag: String,
        description: String
    ): CreateListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    firebaseFireStore
                        .collection("lists")
                        .document(listId)
                        .update(
                            mapOf(
                                "name" to name,
                                "tag" to tag,
                                "description" to description,
                            ),
                        )

                    continuation.resume(CreateListState.Success)
                }
            } else {
                continuation.resume(CreateListState.Error("hive_state_no_user"))
            }
        }

    override suspend fun shareList(
        listId: String,
        email: String,
    ): ShareListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val listRef = firebaseFireStore.collection("lists").document(listId)

                    listRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val sharedIds =
                                documentSnapshot.toObject(ListModel::class.java)?.sharedIds

                            if (sharedIds != null && sharedIds.contains(email)) {
                                continuation.resume(ShareListState.Error("Ten użytkownik ma dostęp do tej listy."))
                            } else {
                                listRef.update("sharedIds", FieldValue.arrayUnion(email))
                                    .addOnSuccessListener {
                                        continuation.resume(ShareListState.Success)
                                    }
                                    .addOnFailureListener { exception ->
                                        continuation.resume(ShareListState.Error("${exception.message}"))
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(ShareListState.Error("${exception.message}"))
                        }

                }
            } else {
                continuation.resume(ShareListState.Error("hive_state_no_user"))
            }
        }

    override suspend fun removeSharedList(
        listId: String,
        email: String,
    ): RemoveSharedListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val listRef = firebaseFireStore.collection("lists").document(listId)

                    listRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val sharedIds =
                                documentSnapshot.toObject(ListModel::class.java)?.sharedIds

                            if (sharedIds != null && !sharedIds.contains(email)) {
                                Log.d("LOG_H", "Ten użytkownik nie ma dostępu do tej listy.")
                                Log.d("LOG_H", email)

                                continuation.resume(RemoveSharedListState.Error("Ten użytkownik nie ma dostępu do tej listy."))
                            } else {
                                listRef.update("sharedIds", FieldValue.arrayRemove(email))
                                    .addOnSuccessListener {
                                        continuation.resume(RemoveSharedListState.Success)
                                    }
                                    .addOnFailureListener { exception ->

                                        Log.d("LOG_H", "${exception.message}")

                                        continuation.resume(RemoveSharedListState.Error("${exception.message}"))
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("LOG_H", "${exception.message}")

                            continuation.resume(RemoveSharedListState.Error("${exception.message}"))
                        }

                }
            } else {
                continuation.resume(RemoveSharedListState.Error("hive_state_no_user"))
            }
        }

    override suspend fun removeList(
        listId: String,
    ): RemoveListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    firebaseFireStore
                        .collection("lists")
                        .document(listId)
                        .delete()
                        .addOnSuccessListener {
                            firebaseFireStore
                                .collection("items")
                                .whereEqualTo("listId", listId)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        document.reference.delete()
                                            .addOnFailureListener { e ->
                                                continuation.resume(RemoveListState.Error("Error deleting document: $e"))
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    continuation.resume(RemoveListState.Error("exception"))
                                }

                            continuation.resume(RemoveListState.Success)
                        }
                        .addOnFailureListener {
                            continuation.resume(RemoveListState.Error("exception"))
                        }
                }
            } else {
                continuation.resume(RemoveListState.Error("hive_state_no_user"))
            }
        }
}