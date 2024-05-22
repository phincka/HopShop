package com.example.hopshop.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import com.example.hopshop.presentation.createList.CreateListState
import com.example.hopshop.presentation.dashboard.ListsState
import com.example.hopshop.presentation.list.ListState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import java.time.LocalDate
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
                                itemsCount = itemsCount
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
                                itemsCount = ItemsCountModel(0, 0)
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


    override suspend fun createList(
        name: String,
        tag: String,
        sharedMail: String,
        description: String
    ): CreateListState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val docRef = firebaseFireStore
                        .collection("lists")
                        .document()
                    val id = docRef.id

                    val list = ListModel(
                        id = id,
                        name = name,
                        ownerId = currentUser.uid,
                        description = description,
                        tag = tag,
                        sharedIds = listOf(sharedMail),
                        isShared = sharedMail.isNotEmpty(),
                        itemsCount = ItemsCountModel(0, 0)
                    )

                    docRef.set(list)

                    continuation.resume(CreateListState.Redirect(listId = id))
                }
            } else {
                continuation.resume(CreateListState.Error("hive_state_no_user"))
            }
        }
}