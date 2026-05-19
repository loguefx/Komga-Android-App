package com.komga.android.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komga.android.ui.components.BookCard
import com.komga.android.ui.components.ErrorMessage
import com.komga.android.ui.components.LoadingIndicator
import com.komga.android.ui.components.SectionHeader
import com.komga.android.ui.components.SeriesCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeriesClick: (String) -> Unit,
    onBookClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadHome() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                TopAppBar(
                    title = {
                        Text(
                            text = "Komga",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }

            if (uiState.isLoading && uiState.onDeckBooks.isEmpty() && uiState.newSeries.isEmpty()) {
                item {
                    LoadingIndicator()
                }
                return@LazyColumn
            }

            if (uiState.errorMessage != null && uiState.onDeckBooks.isEmpty() && uiState.newSeries.isEmpty()) {
                item {
                    ErrorMessage(message = uiState.errorMessage ?: "Something went wrong")
                }
                return@LazyColumn
            }

            // Continue Reading section
            if (uiState.onDeckBooks.isNotEmpty()) {
                item {
                    SectionHeader(title = "Continue Reading")
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.onDeckBooks, key = { it.first.id }) { (book, thumbnailUrl) ->
                            BookCard(
                                title = if (book.seriesTitle.isNotBlank()) "${book.seriesTitle} #${book.number.toInt()}" else book.name,
                                thumbnailUrl = thumbnailUrl,
                                currentPage = book.currentPage,
                                totalPages = book.pagesCount,
                                onClick = { onBookClick(book.id) },
                                modifier = Modifier.width(130.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Recently Added section
            if (uiState.newSeries.isNotEmpty()) {
                item {
                    SectionHeader(title = "Recently Added")
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.newSeries, key = { it.first.id }) { (series, thumbnailUrl) ->
                            SeriesCard(
                                series = series,
                                thumbnailUrl = thumbnailUrl,
                                onClick = { onSeriesClick(series.id) },
                                modifier = Modifier.width(130.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (uiState.onDeckBooks.isEmpty() && uiState.newSeries.isEmpty() && !uiState.isLoading) {
                item {
                    ErrorMessage(message = "No content found.\nStart reading some manga!")
                }
            }
        }
    }
}
