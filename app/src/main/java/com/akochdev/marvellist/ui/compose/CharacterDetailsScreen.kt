package com.akochdev.marvellist.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.marvellist.R
import com.akochdev.marvellist.ui.state.CharacterDetailScreenUIState
import com.akochdev.marvellist.ui.theme.GeneralBackgroundColor
import com.akochdev.marvellist.ui.theme.typography
import com.akochdev.marvellist.viewmodel.CharacterDetailViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun CharacterDetailScreen(
    characterId: String?,
    navController: NavController,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.characterDetail.observeAsState(CharacterDetailScreenUIState(isLoading = true))
    viewModel.fetchCharacterDetails(characterId)
    when {
        uiState.characterData != null -> ContentCharacterDetail(item = uiState.characterData!!)
        uiState.isLoading -> LoadingScreen()
        uiState.isEmptyState -> EmptyCharacterDetailScreen()
        uiState.isErrorState -> ErrorCharacterDetailScreen()
    }
}

@ExperimentalCoilApi
@Composable
fun ContentCharacterDetail(
    item: CharacterDetailModel
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(GeneralBackgroundColor)) {
        Image(
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f),
            painter = rememberImagePainter(
                data = item.pictureUrl,
                builder = {
                    placeholder(R.drawable.marvel_placeholder)
                    error(R.drawable.marvel_placeholder)
                }
            ),
            contentDescription = item.name
        )
        Text(text = item.name, style = typography.h6, modifier = Modifier.padding(top = 8.dp))
        if (item.description.isNotEmpty()) {
            Text(
                text = item.description,
                style = typography.body1,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun EmptyCharacterDetailScreen() {
    Text("Empty screen")
}

@Composable
fun ErrorCharacterDetailScreen() {
    Text("Error screen")
}
