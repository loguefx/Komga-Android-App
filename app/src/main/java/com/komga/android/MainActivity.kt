package com.komga.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komga.android.data.local.PreferencesDataStore
import com.komga.android.ui.navigation.KomgaNavGraph
import com.komga.android.ui.navigation.Screen
import com.komga.android.ui.theme.KomgaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        var startDestination = Screen.Login.route
        runBlocking {
            val isLoggedIn = preferencesDataStore.isLoggedIn().first()
            if (isLoggedIn) {
                startDestination = Screen.Home.route
            }
        }

        splashScreen.setKeepOnScreenCondition { false }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KomgaTheme {
                KomgaNavGraph(startDestination = startDestination)
            }
        }
    }
}
