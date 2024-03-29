package com.akochdev.marvellist.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.marvellist.R
import com.akochdev.marvellist.ui.compose.CharacterListScreenTestTags.*
import com.akochdev.marvellist.ui.navigation.Destination.CHARACTER_DETAIL_SCREEN
import com.akochdev.marvellist.ui.state.CharacterListScreenUIState
import com.akochdev.marvellist.ui.theme.GeneralBackgroundColor
import com.akochdev.marvellist.ui.theme.typography
import com.akochdev.marvellist.viewmodel.CharacterListViewModel

sealed class CharacterListScreenTestTags(val tag: String) {
    object MainContainer : CharacterDetailScreenTestTags("CHARACTER_LIST_SCREEN_CONTAINER_TEST_TAG")
    object LoadMoreItem: CharacterDetailScreenTestTags("CHARACTER_LIST_SCREEN_LOAD_MORE_ITEM_TEST_TAG")
}

@ExperimentalCoilApi
@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.characterList.observeAsState(
        initial = CharacterListScreenUIState(
            isLoading = true
        )
    )
    when {
        uiState.characterData != null -> ContentCharacterListScreen(
            uiState,
            CharacterListViewModel.PAGE_SIZE,
            { position: Int -> viewModel.onChangeScrollPosition(position) },
            { forced -> viewModel.loadMore(forced) }
        ) { characterId ->
            navController.navigate("$CHARACTER_DETAIL_SCREEN/$characterId")
        }
        uiState.isLoading -> LoadingScreen()
        uiState.isEmptyState || uiState.isErrorState -> ErrorScreen(uiState.errorMessage) {
            viewModel.fetchCharacterList()
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ContentCharacterListScreen(
    uiState: CharacterListScreenUIState,
    pageSize: Int,
    onScrollChanged: (Int) -> Unit,
    loadMoreAction: (Boolean) -> Unit,
    goDetailAction: (String) -> Unit
) {
    val listState = rememberLazyListState()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(GeneralBackgroundColor)
            .testTag(MainContainer.tag)
    ) {
        uiState.characterData?.let { list ->
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(list) { index, item ->

                    ContentCharacterListItem(item, goDetailAction)

                    onScrollChanged.invoke(index)
                    if ((index + 1) >= (uiState.currentPage * pageSize) && !uiState.isLoading) {
                        loadMoreAction.invoke(false)
                    }
                }
                item {
                    if (uiState.isErrorLoadingMore) {
                        ContentLoadMoreCharacterListItem(loadMoreAction)
                    }
                }
            }
        }
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { /* Prevent list scrolling while loading more data */ },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ContentCharacterListItem(
    item: CharacterListModel,
    goDetailAction: (String) -> Unit
) {
    Column(modifier = Modifier.clickable { goDetailAction.invoke(item.id) }) {
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
            style = typography.h6,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
        )
        if (item.description.isNotEmpty()) {
            Text(
                text = item.description,
                style = typography.body1,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
        Divider(color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun ContentLoadMoreCharacterListItem(loadMore: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(48.dp)
            .padding(top = 4.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            .background(GeneralBackgroundColor)
            .testTag(LoadMoreItem.tag)
            .clickable { loadMore.invoke(true) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            style = typography.h6.copy(fontSize = 20.sp),
            text = stringResource(R.string.text_character_list_load_more_legend),
            textAlign = TextAlign.Center
        )
    }
}
