package com.example.hopshop.data.repository

import android.content.Context
import android.util.Log
import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ItemsRepository
import com.example.hopshop.domain.repository.ListRepository
import com.example.hopshop.presentation.list.CreateItemState
import com.example.hopshop.presentation.list.RemoveItemState
import com.example.hopshop.presentation.list.SetItemSelectedState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.annotation.Single
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class ItemsRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) : ItemsRepository {
    override suspend fun getItemsByListId(listId: String): List<ItemModel> =
        suspendCoroutine { continuation ->
            val items = mutableListOf<ItemModel>()

            firebaseAuth.currentUser?.let { currentUser ->
                firebaseFireStore
                    .collection("items")
                    .whereEqualTo("listId", listId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val listData = document.data

                            listData?.let { data ->
                                val item = ItemModel(
                                    id = document.id,
                                    listId = data["listId"] as? String ?: "",
                                    name = data["name"] as? String ?: "",
                                    creatorId = data["creatorId"] as? String ?: "",
                                    selected = data["selected"] as? Boolean ?: false,
                                )
                                items.add(item)
                            }
                        }

                        continuation.resume(items)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } ?: continuation.resume(items)
        }

    override suspend fun createItem(
        name: String,
        listId: String,
    ): String =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    val docRef = firebaseFireStore
                        .collection("items")
                        .document()
                    val id = docRef.id

                    val item = ItemModel(
                        id = id,
                        listId = listId,
                        name = name,
                        creatorId = currentUser.uid,
                        selected = false,
                    )

                    docRef.set(item)

                    continuation.resume(id)
                }
            } else {
                continuation.resume("hive_state_no_user")
            }
        }

    override suspend fun removeItem(
        itemId: String,
    ): RemoveItemState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    firebaseFireStore
                        .collection("items")
                        .document(itemId)
                        .delete()
                        .addOnSuccessListener { continuation.resume(RemoveItemState.Success) }
                        .addOnFailureListener { continuation.resume(RemoveItemState.Error("exception")) }
                }
            } else {
                continuation.resume(RemoveItemState.Error("hive_state_no_user"))
            }
        }

    override suspend fun setItemSelected(
        itemId: String,
        isSelected: Boolean
    ): SetItemSelectedState =
        suspendCancellableCoroutine { continuation ->
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.currentUser?.let { currentUser ->
                    firebaseFireStore
                        .collection("items")
                        .document(itemId)
                        .update("selected", isSelected)

                    Log.w("LOG_HIN", "suspendCancellableCoroutine: $isSelected")


                    continuation.resume(SetItemSelectedState.Success)
                }
            } else {
                continuation.resume(SetItemSelectedState.Error("hive_state_no_user"))
            }
        }
}