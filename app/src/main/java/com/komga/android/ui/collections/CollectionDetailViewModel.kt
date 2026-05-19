package com.komga.android.ui.collections

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.repository.KomgaRepository
import com.komga.android.data.repository.Result
import com.komga.android.domain.model.Series
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionDetailUiState(
    val collectionName: String = "",
    val series: List<Pair<Series, String>> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: KomgaRepository
) : ViewModel() {

    private val collectionId: String = savedStateHandle["collectionId"] ?: ""

    private val _uiState = MutableStateFlow(CollectionDetailUiState())
    val uiState: StateFlow<CollectionDetailUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getSeriesByCollection(collectionId)) {
                is Result.Success -> {
                    val withUrls = result.data.map { s ->
                        Pair(s, repository.buildThumbnailUrl(s.id))
                    }
                    _uiState.update { it.copy(series = withUrls, isLoading = false) }
                }
                is Result.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
            }
        }
    }
}
