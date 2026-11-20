package com.example.mylibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mylibrary.di.AppViewModelFactory
import com.example.mylibrary.ui.screens.BookDetailRoute
import com.example.mylibrary.ui.screens.BookFormRoute
import com.example.mylibrary.ui.screens.LibraryRoute
import com.example.mylibrary.ui.screens.LoginRoute
import com.example.mylibrary.ui.screens.NEW_BOOK_ID
import com.example.mylibrary.ui.screens.RegisterRoute
import com.example.mylibrary.ui.screens.SettingsRoute
import com.example.mylibrary.ui.screens.SplashScreen

@Composable
fun AppNavigation(factory: AppViewModelFactory) {
    val navController = rememberNavController()

    val goToLibrary: () -> Unit = {
        navController.navigate(Routes.LIBRARY) {
            popUpTo(Routes.LOGIN) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Routes.LOGIN) {
            LoginRoute(
                factory = factory.login,
                onLoggedIn = goToLibrary,
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterRoute(
                factory = factory.register,
                onBack = { navController.popBackStack() },
                onRegistered = goToLibrary
            )
        }

        composable(Routes.LIBRARY) {
            LibraryRoute(
                factory = factory.library,
                onBookClick = { navController.navigate(Routes.detail(it)) },
                onAddBook = { navController.navigate(Routes.form(NEW_BOOK_ID)) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("bookId") ?: NEW_BOOK_ID
            BookDetailRoute(
                factory = factory.detail(id),
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.form(it)) }
            )
        }

        composable(
            route = Routes.FORM,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("bookId") ?: NEW_BOOK_ID
            BookFormRoute(
                factory = factory.form(id),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsRoute(
                factory = factory.settings,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LIBRARY) { inclusive = true }
                    }
                }
            )
        }
    }
}
