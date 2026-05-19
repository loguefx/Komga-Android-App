package com.komga.android.ui.readlists

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

data class ReadListDetailUiState(
    val readListName: String = "",
    val books: List<Pair<Book, String>> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ReadListDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: KomgaRepository
) : ViewModel() {

    private val readListId: String = savedStateHandle["readListId"] ?: ""

    private val _uiState = MutableStateFlow(ReadListDetailUiState())
    val uiState: StateFlow<ReadListDetailUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getBooksByReadList(readListId)) {
                is Result.Success -> {
                    val withUrls = result.data.map { b ->
                        Pair(b, repository.buildBookThumbnailUrl(b.id))
                    }
                    _uiState.update { it.copy(books = withUrls, isLoading = false) }
                }
                is Result.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
            }
        }
    }
}
