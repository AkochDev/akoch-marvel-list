package com.akochdev.marvellist.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        uiState.characterData != null -> ContentCharacterDetail(
            item = uiState.characterData!!,
            navController = navController
        )
        uiState.isLoading -> LoadingScreen()
        uiState.isEmptyState || uiState.isErrorState -> ErrorScreen {
            viewModel.fetchCharacterDetails(characterId)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ContentCharacterDetail(
    navController: NavController,
    item: CharacterDetailModel
) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = GeneralBackgroundColor) {
                Image(
                    modifier = Modifier.clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24_white),
                    contentDescription = stringResource(
                        id = R.string.image_go_back_arrow
                    )
                )
                Text(
                    text = item.name,
                    style = typography.h6.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GeneralBackgroundColor)
                .scrollable(
                    enabled = true,
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                )
        ) {
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
            Text(
                text = item.name,
                textAlign = TextAlign.Center,
                style = typography.h6.copy(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 20.dp, end = 20.dp)
            )
            if (item.description.isNotEmpty()) {
                Text(
                    text = item.description,
                    style = typography.body1,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 20.dp, end = 20.dp)
                )
            }
        }
    }
}
