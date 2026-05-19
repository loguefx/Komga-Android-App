package com.komga.android.domain.model

data class Collection(
    val id: String,
    val name: String,
    val seriesCount: Int = 0,
    val ordered: Boolean = false
)

data class ReadList(
    val id: String,
    val name: String,
    val bookCount: Int = 0
)
