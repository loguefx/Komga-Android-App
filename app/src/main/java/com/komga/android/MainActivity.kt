package com.komga.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        // Must be called before super.onCreate()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }

        // Hilt field injection happens inside super.onCreate()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Now preferencesDataStore is safely injected
        val startDestination = runBlocking {
            if (preferencesDataStore.isLoggedIn().first()) Screen.Home.route
            else Screen.Login.route
        }

        setContent {
            KomgaTheme {
                KomgaNavGraph(startDestination = startDestination)
            }
        }
    }
}
