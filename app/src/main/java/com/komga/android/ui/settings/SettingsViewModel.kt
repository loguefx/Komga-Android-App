package com.komga.android.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komga.android.data.local.PreferencesDataStore
import com.komga.android.data.local.ThemeMode
import com.komga.android.data.repository.KomgaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: KomgaRepository,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    var defaultRtl by mutableStateOf(false)
        private set

    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    init {
        viewModelScope.launch {
            defaultRtl = preferencesDataStore.getDefaultRtl().first()
            themeMode = preferencesDataStore.getThemeMode().first()
        }
    }

    fun setDefaultRtl(value: Boolean) {
        defaultRtl = value
        viewModelScope.launch { preferencesDataStore.saveDefaultRtl(value) }
    }

    fun setThemeMode(mode: ThemeMode) {
        themeMode = mode
        viewModelScope.launch { preferencesDataStore.saveThemeMode(mode) }
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }
}
