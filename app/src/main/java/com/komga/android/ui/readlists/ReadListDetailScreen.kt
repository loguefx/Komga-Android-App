package com.komga.android.ui.readlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadListDetailScreen(
    readListName: String,
    onBookClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ReadListDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(readListName.ifBlank { "Reading List" }, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::load,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.isLoading && uiState.books.isEmpty() -> LoadingIndicator()
                uiState.errorMessage != null && uiState.books.isEmpty() ->
                    ErrorMessage(message = uiState.errorMessage ?: "Something went wrong")
                else -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 8.dp,
                        bottom = 16.dp, start = 8.dp, end = 8.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.books, key = { it.first.id }) { (book, url) ->
                        val label = if (book.seriesTitle.isNotBlank())
                            "${book.seriesTitle} #${book.number.let { n ->
                                if (n == n.toLong().toFloat()) n.toLong().toString() else n.toString()
                            }}"
                        else book.name
                        BookCard(
                            title = label,
                            thumbnailUrl = url,
                            currentPage = book.currentPage,
                            totalPages = book.pagesCount,
                            onClick = { onBookClick(book.id) }
                        )
                    }
                }
            }
        }
    }
}
