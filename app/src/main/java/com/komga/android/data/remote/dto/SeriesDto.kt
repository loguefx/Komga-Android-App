package com.komga.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SeriesDto(
    @SerializedName("id") val id: String,
    @SerializedName("libraryId") val libraryId: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String = "",
    @SerializedName("booksCount") val booksCount: Int = 0,
    @SerializedName("booksReadCount") val booksReadCount: Int = 0,
    @SerializedName("booksUnreadCount") val booksUnreadCount: Int = 0,
    @SerializedName("booksInProgressCount") val booksInProgressCount: Int = 0,
    @SerializedName("metadata") val metadata: SeriesMetadataDto = SeriesMetadataDto(),
    @SerializedName("createdDate") val createdDate: String = "",
    @SerializedName("lastModifiedDate") val lastModifiedDate: String = ""
)

data class SeriesMetadataDto(
    @SerializedName("status") val status: String = "ONGOING",
    @SerializedName("statusLock") val statusLock: Boolean = false,
    @SerializedName("title") val title: String = "",
    @SerializedName("titleLock") val titleLock: Boolean = false,
    @SerializedName("titleSort") val titleSort: String = "",
    @SerializedName("titleSortLock") val titleSortLock: Boolean = false,
    @SerializedName("summary") val summary: String = "",
    @SerializedName("summaryLock") val summaryLock: Boolean = false,
    @SerializedName("readingDirection") val readingDirection: String? = null,
    @SerializedName("readingDirectionLock") val readingDirectionLock: Boolean = false,
    @SerializedName("publisher") val publisher: String = "",
    @SerializedName("publisherLock") val publisherLock: Boolean = false,
    @SerializedName("ageRating") val ageRating: Int? = null,
    @SerializedName("ageRatingLock") val ageRatingLock: Boolean = false,
    @SerializedName("language") val language: String = "",
    @SerializedName("languageLock") val languageLock: Boolean = false,
    @SerializedName("genres") val genres: List<String> = emptyList(),
    @SerializedName("genresLock") val genresLock: Boolean = false,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("tagsLock") val tagsLock: Boolean = false,
    @SerializedName("totalBookCount") val totalBookCount: Int? = null,
    @SerializedName("totalBookCountLock") val totalBookCountLock: Boolean = false,
    @SerializedName("sharingLabels") val sharingLabels: List<String> = emptyList(),
    @SerializedName("sharingLabelsLock") val sharingLabelsLock: Boolean = false,
    @SerializedName("created") val created: String = "",
    @SerializedName("lastModified") val lastModified: String = ""
)

data class PagedSeriesDto(
    @SerializedName("content") val content: List<SeriesDto> = emptyList(),
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

data class PageableDto(
    @SerializedName("sort") val sort: SortDto = SortDto(),
    @SerializedName("pageNumber") val pageNumber: Int = 0,
    @SerializedName("pageSize") val pageSize: Int = 20,
    @SerializedName("offset") val offset: Int = 0,
    @SerializedName("paged") val paged: Boolean = true,
    @SerializedName("unpaged") val unpaged: Boolean = false
)

data class SortDto(
    @SerializedName("sorted") val sorted: Boolean = false,
    @SerializedName("unsorted") val unsorted: Boolean = true,
    @SerializedName("empty") val empty: Boolean = true
)
