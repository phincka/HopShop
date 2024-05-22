package com.example.hopshop.data.model

data class ListModel (
    val id: String,
    val name: String,
    val ownerId: String,
    val description: String,
    val tag: String,
    val sharedIds: List<String>,
    val isShared: Boolean,
    val itemsCount: ItemsCountModel,
)