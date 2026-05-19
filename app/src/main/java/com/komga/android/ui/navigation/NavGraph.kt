package com.komga.android.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.komga.android.ui.collections.CollectionDetailScreen
import com.komga.android.ui.favorites.FavoritesScreen
import com.komga.android.ui.home.HomeScreen
import com.komga.android.ui.library.LibraryScreen
import com.komga.android.ui.login.LoginScreen
import com.komga.android.ui.reader.ReaderScreen
import com.komga.android.ui.readlists.ReadListDetailScreen
import com.komga.android.ui.search.SearchScreen
import com.komga.android.ui.series.SeriesDetailScreen
import com.komga.android.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Library : Screen("library")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object SeriesDetail : Screen("series/{seriesId}") {
        fun createRoute(seriesId: String) = "series/$seriesId"
    }
    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: String) = "reader/$bookId"
    }
    object CollectionDetail : Screen("collection/{collectionId}?name={collectionName}") {
        fun createRoute(id: String, name: String) = "collection/$id?name=${name.encodeUrl()}"
    }
    object ReadListDetail : Screen("readlist/{readListId}?name={readListName}") {
        fun createRoute(id: String, name: String) = "readlist/$id?name=${name.encodeUrl()}"
    }
}

private fun String.encodeUrl() = java.net.URLEncoder.encode(this, "UTF-8")

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Browse", Screen.Library.route, Icons.AutoMirrored.Filled.LibraryBooks, Icons.AutoMirrored.Outlined.LibraryBooks),
    BottomNavItem("Search", Screen.Search.route, Icons.Filled.Search, Icons.Outlined.Search),
    BottomNavItem("Favorites", Screen.Favorites.route, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
)

@Composable
fun KomgaNavGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            }
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onSeriesClick = { navController.navigate(Screen.SeriesDetail.createRoute(it)) },
                    onBookClick   = { navController.navigate(Screen.Reader.createRoute(it)) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Library.route) {
                LibraryScreen(
                    onSeriesClick = { navController.navigate(Screen.SeriesDetail.createRoute(it)) },
                    onCollectionClick = { id, name ->
                        navController.navigate(Screen.CollectionDetail.createRoute(id, name))
                    },
                    onReadListClick = { id, name ->
                        navController.navigate(Screen.ReadListDetail.createRoute(id, name))
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    onSeriesClick = { navController.navigate(Screen.SeriesDetail.createRoute(it)) },
                    onBookClick   = { navController.navigate(Screen.Reader.createRoute(it)) }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onSeriesClick = { navController.navigate(Screen.SeriesDetail.createRoute(it)) }
                )
            }

            composable(
                route = Screen.CollectionDetail.route,
                arguments = listOf(
                    navArgument("collectionId") { type = NavType.StringType },
                    navArgument("collectionName") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStack ->
                val name = backStack.arguments?.getString("collectionName") ?: ""
                CollectionDetailScreen(
                    collectionName = java.net.URLDecoder.decode(name, "UTF-8"),
                    onSeriesClick = { navController.navigate(Screen.SeriesDetail.createRoute(it)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.ReadListDetail.route,
                arguments = listOf(
                    navArgument("readListId") { type = NavType.StringType },
                    navArgument("readListName") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStack ->
                val name = backStack.arguments?.getString("readListName") ?: ""
                ReadListDetailScreen(
                    readListName = java.net.URLDecoder.decode(name, "UTF-8"),
                    onBookClick = { navController.navigate(Screen.Reader.createRoute(it)) },
                    onBack = { navController.popBackStack() }
                )
            }

            // ── Series Detail — deep link: komga://series/{seriesId}
            composable(
                route = Screen.SeriesDetail.route,
                arguments = listOf(navArgument("seriesId") { type = NavType.StringType }),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "komga://series/{seriesId}" }
                )
            ) { backStack ->
                SeriesDetailScreen(
                    seriesId = backStack.arguments?.getString("seriesId") ?: "",
                    onBookClick = { navController.navigate(Screen.Reader.createRoute(it)) },
                    onBack = { navController.popBackStack() }
                )
            }

            // ── Reader — deep link: komga://book/{bookId}
            composable(
                route = Screen.Reader.route,
                arguments = listOf(navArgument("bookId") { type = NavType.StringType }),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "komga://book/{bookId}" }
                ),
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(300))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(300))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(300))
                }
            ) { backStack ->
                ReaderScreen(
                    bookId = backStack.arguments?.getString("bookId") ?: "",
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
