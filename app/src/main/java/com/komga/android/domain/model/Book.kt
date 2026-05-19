package com.komga.android.domain.model

data class Book(
    val id: String,
    val seriesId: String,
    val seriesTitle: String,
    val name: String,
    val number: Float,
    val pagesCount: Int,
    val currentPage: Int,
    val completed: Boolean,
    val mediaType: String,
    val size: String
) {
    val readingProgress: Float
        get() = if (pagesCount > 0) currentPage.toFloat() / pagesCount else 0f
}
