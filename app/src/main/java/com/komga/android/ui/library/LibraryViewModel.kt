package com.komga.android.ui.library

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

enum class SortOption(val label: String, val apiSort: String) {
    TITLE("Title A–Z", "metadata.titleSort,asc"),
    TITLE_DESC("Title Z–A", "metadata.titleSort,desc"),
    RECENTLY_ADDED("Recently Added", "createdDate,desc"),
    OLDEST_ADDED("Oldest Added", "createdDate,asc"),
    RECENTLY_UPDATED("Recently Updated", "lastModifiedDate,desc")
}

data class LibraryUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val seriesList: List<Pair<Series, String>> = emptyList(),
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.TITLE,
    val showSortMenu: Boolean = false,
    val errorMessage: String? = null,
    val hasMore: Boolean = false,
    val currentPage: Int = 0
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: KomgaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadSeries(reset = true)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSortSelected(sort: SortOption) {
        _uiState.update { it.copy(sortOption = sort, showSortMenu = false) }
        loadSeries(reset = true)
    }

    fun toggleSortMenu() {
        _uiState.update { it.copy(showSortMenu = !it.showSortMenu) }
    }

    fun loadSeries(reset: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore) return

        val page = if (reset) 0 else state.currentPage + 1
        val sort = state.sortOption.apiSort

        viewModelScope.launch {
            _uiState.update {
                if (reset) it.copy(isLoading = true, errorMessage = null, currentPage = 0)
                else it.copy(isLoadingMore = true)
            }

            when (val result = repository.getSeries(page = page, size = 30, sort = sort)) {
                is Result.Success -> {
                    val (series, hasMore) = result.data
                    val seriesWithUrls = series.map { s ->
                        Pair(s, repository.buildThumbnailUrl(s.id))
                    }

                    _uiState.update { current ->
                        val newList = if (reset) seriesWithUrls
                        else current.seriesList + seriesWithUrls
                        current.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            seriesList = newList,
                            hasMore = hasMore,
                            currentPage = page
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun loadMore() {
        if (!_uiState.value.hasMore || _uiState.value.isLoadingMore) return
        loadSeries(reset = false)
    }

    fun refresh() {
        loadSeries(reset = true)
    }
}
