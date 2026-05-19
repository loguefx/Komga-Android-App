package com.komga.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.repository.KomgaRepository
import com.komga.android.data.repository.Result
import com.komga.android.domain.model.Book
import com.komga.android.domain.model.Series
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val onDeckBooks: List<Pair<Book, String>> = emptyList(),
    val newSeries: List<Pair<Series, String>> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KomgaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Load on-deck books and new series in parallel
            val onDeckResult = repository.getBooksOnDeck(size = 20)
            val newSeriesResult = repository.getLatestSeries(size = 20)

            val onDeckBooks = when (onDeckResult) {
                is Result.Success -> onDeckResult.data.map { book ->
                    val url = repository.buildBookThumbnailUrl(book.id)
                    Pair(book, url)
                }
                is Result.Error -> emptyList()
            }

            val newSeries = when (newSeriesResult) {
                is Result.Success -> newSeriesResult.data.map { series ->
                    val url = repository.buildThumbnailUrl(series.id)
                    Pair(series, url)
                }
                is Result.Error -> emptyList()
            }

            val error = when {
                onDeckResult is Result.Error && newSeriesResult is Result.Error ->
                    "Failed to load content. Check your connection."
                else -> null
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    onDeckBooks = onDeckBooks,
                    newSeries = newSeries,
                    errorMessage = error
                )
            }
        }
    }
}
