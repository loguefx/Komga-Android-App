package com.komga.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "komga_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val KEY_SERVER_URL = stringPreferencesKey("server_url")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_PASSWORD = stringPreferencesKey("password")
        private val KEY_DEFAULT_RTL = booleanPreferencesKey("default_rtl")
    }

    fun getServerUrl(): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_SERVER_URL] ?: ""
    }

    fun getCredentials(): Flow<Pair<String, String>?> = context.dataStore.data.map { prefs ->
        val email = prefs[KEY_EMAIL]
        val password = prefs[KEY_PASSWORD]
        if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
            Pair(email, password)
        } else null
    }

    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        val url = prefs[KEY_SERVER_URL]
        val email = prefs[KEY_EMAIL]
        val password = prefs[KEY_PASSWORD]
        !url.isNullOrBlank() && !email.isNullOrBlank() && !password.isNullOrBlank()
    }

    suspend fun saveLoginInfo(serverUrl: String, email: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SERVER_URL] = serverUrl.trimEnd('/')
            prefs[KEY_EMAIL] = email
            prefs[KEY_PASSWORD] = password
        }
    }

    fun getDefaultRtl(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_RTL] ?: false
    }

    suspend fun saveDefaultRtl(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_DEFAULT_RTL] = enabled }
    }

    suspend fun clearLoginInfo() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_SERVER_URL)
            prefs.remove(KEY_EMAIL)
            prefs.remove(KEY_PASSWORD)
        }
    }
}
