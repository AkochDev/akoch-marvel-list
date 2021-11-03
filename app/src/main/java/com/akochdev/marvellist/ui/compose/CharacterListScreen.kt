package com.akochdev.marvellist.ui.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.marvellist.ui.state.CharacterListScreenUIState
import com.akochdev.marvellist.viewmodel.CharacterListViewModel

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.fetchCharacterList()
        .observeAsState(CharacterListScreenUIState(isLoading = true))
    val characterData = uiState.characterData
    when {
        characterData != null -> ContentCharacterListScreen(characterData) {
            navController.navigate("") // TODO add navigation
        }
        uiState.isEmptyState -> EmptyCharacterListScreen {
            viewModel.fetchCharacterList()
        }
        uiState.isLoading -> LoadingCharacterListScreen()
        uiState.isErrorState -> ErrorCharacterListScreen(uiState.errorMessage) {
            viewModel.fetchCharacterList()
        }
    }
}

@Composable
fun ContentCharacterListScreen(data: CharacterListModel, goDetailAction: (String) -> Unit) {
    Text("${data.items.size}")
}

@Composable
fun EmptyCharacterListScreen(reloadAction: () -> Unit) {
    Text("Empty state")
}

@Composable
fun LoadingCharacterListScreen() {
    Text("Loading")
}

@Composable
fun ErrorCharacterListScreen(errorMessage: String?, reloadAction: () -> Unit) {
    Text("Error $errorMessage")
}
