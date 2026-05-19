package com.komga.android.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.local.FavoriteEntity
import com.komga.android.data.repository.KomgaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: List<Pair<FavoriteEntity, String>> = emptyList(),
    val isEmpty: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: KomgaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavorites().collect { favorites ->
                val favoritesWithUrls = favorites.map { entity ->
                    Pair(entity, repository.buildThumbnailUrl(entity.seriesId))
                }
                _uiState.update { it.copy(favorites = favoritesWithUrls, isEmpty = favorites.isEmpty()) }
            }
        }
    }

    fun removeFavorite(seriesId: String) {
        viewModelScope.launch {
            repository.removeFavorite(seriesId)
        }
    }
}
