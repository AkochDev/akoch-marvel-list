package com.akochdev.marvellist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.akochdev.marvellist.ui.compose.CharacterDetailScreen
import com.akochdev.marvellist.ui.compose.CharacterListScreen
import com.akochdev.marvellist.ui.navigation.Destination.CHARACTER_DETAIL_SCREEN
import com.akochdev.marvellist.ui.navigation.Destination.CHARACTER_LIST_SCREEN
import com.akochdev.marvellist.ui.navigation.Destination.Parameters.CHARACTER_ID
import kotlinx.coroutines.InternalCoroutinesApi

object Destination {
    const val CHARACTER_LIST_SCREEN = "CHARACTER_LIST_SCREEN"
    const val CHARACTER_DETAIL_SCREEN = "CHARACTER_DETAIL_SCREEN"

    object Parameters {
        const val CHARACTER_ID = "characterId"
    }
}
@InternalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CHARACTER_LIST_SCREEN) {
        composable(CHARACTER_LIST_SCREEN) {
            CharacterListScreen(navController)
        }
        composable("$CHARACTER_DETAIL_SCREEN/{$CHARACTER_ID}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString(CHARACTER_ID)
            CharacterDetailScreen(characterId, navController)
        }
    }
}
