package com.akochdev.marvellist.ui.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akochdev.marvellist.ui.compose.CharacterListScreen
import com.akochdev.marvellist.ui.navigation.Destination.CHARACTER_DETAIL_SCREEN
import com.akochdev.marvellist.ui.navigation.Destination.CHARACTER_LIST_SCREEN
import com.akochdev.marvellist.ui.navigation.Destination.Parameters.CHARACTER_ID
import com.akochdev.marvellist.ui.navigation.Screen.CharacterDetail
import com.akochdev.marvellist.ui.navigation.Screen.CharacterList

sealed class Screen(val route: String) {
    object CharacterList : Screen(CHARACTER_LIST_SCREEN)
    object CharacterDetail : Screen("$CHARACTER_DETAIL_SCREEN/${CHARACTER_ID}")
}

object Destination {
    const val CHARACTER_LIST_SCREEN = "CHARACTER_LIST_SCREEN"
    const val CHARACTER_DETAIL_SCREEN = "CHARACTER_DETAIL_SCREEN"

    object Parameters {
        const val CHARACTER_ID = "characterId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CharacterList.route) {
        composable(CharacterList.route) {
            CharacterListScreen(navController)
        }
        composable(CharacterDetail.route) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString(CHARACTER_ID)
            Text("Detail") // TODO add detail page
        }
    }
}
