package com.komga.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val seriesId: String,
    val title: String,
    val libraryId: String,
    val booksCount: Int,
    val booksReadCount: Int,
    val savedAt: Long = System.currentTimeMillis()
)
