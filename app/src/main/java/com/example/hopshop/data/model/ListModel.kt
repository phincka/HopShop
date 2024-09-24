package com.example.hopshop.data.model

data class ListModel(
    val id: String,
    val name: String,
    val ownerId: String,
    val description: String,
    val tag: String,
    val sharedIds: List<String>,
    val isShared: Boolean,
) {
    constructor() : this(
        "", "", "", "", "", emptyList(), false
    )
}

data class FormListModel(
    val id: String? = null,
    val name: String,
    val description: String,
    val tag: String,
    val sharedIds: List<String>,
) {
    constructor() : this(
        null, "", "", "", emptyList()
    )
}