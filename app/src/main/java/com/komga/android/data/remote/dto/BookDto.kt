package com.komga.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id") val id: String,
    @SerializedName("seriesId") val seriesId: String,
    @SerializedName("seriesTitle") val seriesTitle: String = "",
    @SerializedName("libraryId") val libraryId: String = "",
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String = "",
    @SerializedName("number") val number: Float = 0f,
    @SerializedName("created") val created: String = "",
    @SerializedName("lastModified") val lastModified: String = "",
    @SerializedName("fileLastModified") val fileLastModified: String = "",
    @SerializedName("sizeBytes") val sizeBytes: Long = 0,
    @SerializedName("size") val size: String = "",
    @SerializedName("media") val media: BookMediaDto = BookMediaDto(),
    @SerializedName("metadata") val metadata: BookMetadataDto = BookMetadataDto(),
    @SerializedName("readProgress") val readProgress: ReadProgressDto? = null,
    @SerializedName("deleted") val deleted: Boolean = false
)

data class BookMediaDto(
    @SerializedName("status") val status: String = "READY",
    @SerializedName("mediaType") val mediaType: String = "",
    @SerializedName("pagesCount") val pagesCount: Int = 0,
    @SerializedName("comment") val comment: String = "",
    @SerializedName("epubDivinaCompatible") val epubDivinaCompatible: Boolean = false
)

data class BookMetadataDto(
    @SerializedName("title") val title: String = "",
    @SerializedName("titleLock") val titleLock: Boolean = false,
    @SerializedName("summary") val summary: String = "",
    @SerializedName("summaryLock") val summaryLock: Boolean = false,
    @SerializedName("number") val number: String = "",
    @SerializedName("numberLock") val numberLock: Boolean = false,
    @SerializedName("numberSort") val numberSort: Float = 0f,
    @SerializedName("numberSortLock") val numberSortLock: Boolean = false,
    @SerializedName("releaseDate") val releaseDate: String? = null,
    @SerializedName("releaseDateLock") val releaseDateLock: Boolean = false,
    @SerializedName("authors") val authors: List<AuthorDto> = emptyList(),
    @SerializedName("authorsLock") val authorsLock: Boolean = false,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("tagsLock") val tagsLock: Boolean = false,
    @SerializedName("isbn") val isbn: String = "",
    @SerializedName("isbnLock") val isbnLock: Boolean = false,
    @SerializedName("links") val links: List<LinkDto> = emptyList(),
    @SerializedName("linksLock") val linksLock: Boolean = false,
    @SerializedName("created") val created: String = "",
    @SerializedName("lastModified") val lastModified: String = ""
)

data class AuthorDto(
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String
)

data class LinkDto(
    @SerializedName("label") val label: String,
    @SerializedName("url") val url: String
)

data class ReadProgressDto(
    @SerializedName("page") val page: Int = 1,
    @SerializedName("completed") val completed: Boolean = false,
    @SerializedName("readDate") val readDate: String = "",
    @SerializedName("created") val created: String = "",
    @SerializedName("lastModified") val lastModified: String = "",
    @SerializedName("deviceId") val deviceId: String = "",
    @SerializedName("deviceName") val deviceName: String = ""
)

data class ReadProgressUpdateDto(
    @SerializedName("page") val page: Int,
    @SerializedName("completed") val completed: Boolean
)

data class PagedBookDto(
    @SerializedName("content") val content: List<BookDto> = emptyList(),
    @SerializedName("pageable") val pageable: PageableDto = PageableDto(),
    @SerializedName("totalElements") val totalElements: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0,
    @SerializedName("last") val last: Boolean = false,
    @SerializedName("first") val first: Boolean = true,
    @SerializedName("size") val size: Int = 20,
    @SerializedName("number") val number: Int = 0,
    @SerializedName("numberOfElements") val numberOfElements: Int = 0,
    @SerializedName("empty") val empty: Boolean = true
)
