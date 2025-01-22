package pl.hincka.hopshop.data.repository

import android.util.Log
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.presentation.dashboard.ListsState
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import pl.hincka.hopshop.presentation.list.RemoveListState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import pl.hincka.hopshop.presentation.list.RemoveSharedListState
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
                        Filter.arrayContains("sharedIds", currentUser.uid)
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

    override suspend fun getListByShareCode(shareCode: String): ListModel? =
        suspendCoroutine { continuation ->

            firebaseAuth.currentUser?.let { currentUser ->
                firebaseFireStore
                    .collection("lists")
                    .whereEqualTo("shareCode", shareCode)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.documents.isNotEmpty()) {
                            val hiveData = documentSnapshot.documents.first()
                            val sharedIds = hiveData["sharedIds"] as? List<String> ?: listOf("")

                            val list = ListModel(
                                id = hiveData.id,
                                ownerId = hiveData["ownerId"] as? String ?: "",
                                name = hiveData["name"] as? String ?: "",
                                description = hiveData["description"] as? String ?: "",
                                tag = hiveData["tag"] as? String ?: "",
                                shareCode = hiveData["shareCode"] as? String ?: "",
                                sharedIds = sharedIds,
                                isShared = currentUser.uid in sharedIds,
                            )

                            continuation.resume(list)
                        } else {
                            continuation.resume(null)
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
    ): ShareListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val listRef = firebaseFireStore.collection("lists").document(listId)

                    listRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val sharedIds = documentSnapshot.toObject(ListModel::class.java)?.sharedIds
                            if (sharedIds != null && sharedIds.contains(currentUser.uid)) {
                                continuation.resume(ShareListState.Error("Ten użytkownik ma dostęp do tej listy."))
                            }

                            listRef.update("sharedIds", FieldValue.arrayUnion(currentUser.uid))
                                .addOnSuccessListener {
                                    if (continuation.isActive) {
                                        continuation.resume(ShareListState.Success)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    if (continuation.isActive) {
                                        continuation.resume(ShareListState.Error("${exception.message}"))
                                    }
                                }
                        }
                        .addOnFailureListener { exception ->
                            if (continuation.isActive) {
                                continuation.resume(ShareListState.Error("${exception.message}"))
                            }
                        }
                }
            } else {
                continuation.resume(ShareListState.Error("hive_state_no_user"))
            }
        }

    override suspend fun generateShareCode(
        listId: String,
    ): String? =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let {
                    val shareCode = getRandomCharacters(listId)

                    firebaseFireStore
                        .collection("lists")
                        .document(listId)
                        .update(
                            mapOf(
                                "shareCode" to shareCode,
                            ),
                        )

                    continuation.resume(shareCode)
                }
            } else {
                continuation.resume(null)
            }
        }

    override suspend fun removeSharedList(
        listId: String,
    ): RemoveSharedListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val listRef = firebaseFireStore.collection("lists").document(listId)

                    listRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val sharedIds = documentSnapshot.toObject(ListModel::class.java)?.sharedIds

                            if (sharedIds != null && sharedIds.contains(currentUser.uid)) {
                                listRef.update("sharedIds", FieldValue.arrayRemove(currentUser.uid))
                                    .addOnSuccessListener {
                                        continuation.resume(RemoveSharedListState.Success)
                                    }
                                    .addOnFailureListener { exception ->
                                        continuation.resume(RemoveSharedListState.Error("${exception.message}"))
                                    }
                            } else {
                                continuation.resume(RemoveSharedListState.Error("Nie jesteś w tej liście"))
                            }
                        }
                        .addOnFailureListener { exception ->
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

private fun getRandomCharacters(input: String): String {
    val count = 4
    if (input.length < count) {
        throw IllegalArgumentException("Input string must contain at least $count characters.")
    }
    return input.toList().shuffled().take(count).joinToString("")
}