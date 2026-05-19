package com.komga.android.domain.model

data class Series(
    val id: String,
    val libraryId: String,
    val title: String,
    val booksCount: Int,
    val booksReadCount: Int,
    val booksUnreadCount: Int,
    val booksInProgressCount: Int,
    val status: String,
    val summary: String,
    val genres: List<String>,
    val publisher: String,
    val language: String,
    val isFavorite: Boolean = false
)
