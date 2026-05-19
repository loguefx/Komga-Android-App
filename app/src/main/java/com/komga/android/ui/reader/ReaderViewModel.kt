package com.komga.android.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.repository.KomgaRepository
import com.komga.android.data.repository.Result
import com.komga.android.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReaderUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val pageUrls: List<String> = emptyList(),
    val currentPage: Int = 0,
    val errorMessage: String? = null,
    val showControls: Boolean = true
)

@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: KomgaRepository
) : ViewModel() {

    private val bookId: String = savedStateHandle.get<String>("bookId") ?: ""

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.getBookById(bookId)) {
                is Result.Success -> {
                    val book = result.data
                    val startPage = if (book.completed) 0 else maxOf(0, book.currentPage - 1)
                    val pageUrls = (1..book.pagesCount).map { page ->
                        repository.buildPageUrl(bookId, page)
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            book = book,
                            pageUrls = pageUrls,
                            currentPage = startPage
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    fun onPageChanged(page: Int) {
        val totalPages = _uiState.value.book?.pagesCount ?: return
        _uiState.update { it.copy(currentPage = page) }

        viewModelScope.launch {
            val completed = page >= totalPages - 1
            repository.updateReadProgress(bookId, page + 1, completed)
        }
    }

    fun toggleControls() {
        _uiState.update { it.copy(showControls = !it.showControls) }
    }
}
