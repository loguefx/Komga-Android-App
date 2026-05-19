package com.komga.android.data.remote.dto

data class CollectionDto(
    val id: String = "",
    val name: String = "",
    val ordered: Boolean = false,
    val seriesIds: List<String> = emptyList(),
    val filtered: Boolean = false
)

data class PagedCollectionDto(
    val content: List<CollectionDto> = emptyList(),
    val last: Boolean = true,
    val totalElements: Int = 0,
    val totalPages: Int = 1,
    val number: Int = 0
)

data class ReadListDto(
    val id: String = "",
    val name: String = "",
    val bookIds: List<String> = emptyList(),
    val filtered: Boolean = false
)

data class PagedReadListDto(
    val content: List<ReadListDto> = emptyList(),
    val last: Boolean = true,
    val totalElements: Int = 0,
    val totalPages: Int = 1,
    val number: Int = 0
)
