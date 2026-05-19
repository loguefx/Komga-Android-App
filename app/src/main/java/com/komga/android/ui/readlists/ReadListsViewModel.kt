package com.komga.android.ui.readlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.repository.KomgaRepository
import com.komga.android.data.repository.Result
import com.komga.android.domain.model.ReadList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadListsUiState(
    val readLists: List<ReadList> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ReadListsViewModel @Inject constructor(
    private val repository: KomgaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadListsUiState())
    val uiState: StateFlow<ReadListsUiState> = _uiState.asStateFlow()

    init { loadReadLists() }

    fun loadReadLists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getReadLists()) {
                is Result.Success -> _uiState.update { it.copy(readLists = result.data, isLoading = false) }
                is Result.Error   -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
            }
        }
    }
}
