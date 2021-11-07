package com.akochdev.marvellist.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.marvellist.R
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.*
import com.akochdev.marvellist.ui.state.CharacterDetailScreenUIState
import com.akochdev.marvellist.ui.theme.GeneralBackgroundColor
import com.akochdev.marvellist.ui.theme.typography
import com.akochdev.marvellist.viewmodel.CharacterDetailViewModel
import kotlinx.coroutines.InternalCoroutinesApi

sealed class CharacterDetailScreenTestTags(val tag: String) {
    object Content : CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_TEST_TAG")
    object AppBarName :
        CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_APP_BAR_NAME_TEST_TAG")

    object Name : CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_NAME_TEST_TAG")
    object Description :
        CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_DESCRIPTION_TEST_TAG")

    object AppearancesTitle :
        CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_APPEARANCES_TEST_TAG")

    object WikiLink : CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_WIKI_LINK_TEST_TAG")
    object ComicLink : CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_COMIC_LINK_TEST_TAG")
    object DetailLink :
        CharacterDetailScreenTestTags("CONTENT_CHARACTER_DETAIL_DETAIL_LINK_TEST_TAG")
}

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
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier.testTag(Content.tag),
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
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .testTag(AppBarName.tag)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GeneralBackgroundColor)
                .verticalScroll(scrollState)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    .testTag(Name.tag)
            )
            if (item.description.isNotEmpty()) {
                Text(
                    text = item.description,
                    style = typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                        .testTag(Description.tag)
                )
            }
            if (item.comics.isNotEmpty() || item.series.isNotEmpty() || item.stories.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.text_character_detail_participated_on),
                    textAlign = TextAlign.Center,
                    style = typography.h6.copy(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                        .testTag(AppearancesTitle.tag)
                )
                CharacterDetailsAppearances(
                    stringResource(id = R.string.text_character_detail_comics),
                    item.comics
                )
                CharacterDetailsAppearances(
                    stringResource(id = R.string.text_character_detail_series),
                    item.series
                )
                CharacterDetailsAppearances(
                    stringResource(id = R.string.text_character_detail_stories),
                    item.stories
                )
                if (item.detailUrl.isNullOrEmpty().not()) {
                    VisitLink(
                        modifier = Modifier.testTag(DetailLink.tag),
                        text = stringResource(
                            id = R.string.text_character_detail_visit_details
                        ),
                        navigateToUrl = item.detailUrl!!
                    )
                }
                if (item.comicLinkUrl.isNullOrEmpty().not()) {
                    VisitLink(
                        modifier = Modifier.testTag(ComicLink.tag),
                        text = stringResource(
                            id = R.string.text_character_detail_visit_comics
                        ),
                        navigateToUrl = item.comicLinkUrl!!
                    )
                }
                if (item.wikiLinkUrl.isNullOrEmpty().not()) {
                    VisitLink(
                        modifier = Modifier.testTag(WikiLink.tag),
                        text = stringResource(
                            id = R.string.text_character_detail_visit_comics
                        ),
                        navigateToUrl = item.wikiLinkUrl!!
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterDetailsAppearances(title: String, list: List<String>) {
    if (list.isNotEmpty()) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = typography.h6.copy(fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(100.dp)
        ) {
            items(list) { item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp)
                        .border(BorderStroke(1.dp, Color.White))
                ) {
                    Text(
                        text = item,
                        textAlign = TextAlign.Center,
                        style = typography.h6.copy(fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VisitLink(text: String, navigateToUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = typography.h6.copy(fontSize = 15.sp, textDecoration = TextDecoration.Underline),
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(navigateToUrl)
                }
                startActivity(context, intent, null)
            }
    )
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun ContentCharacterDetailPreview() {
    ContentCharacterDetail(
        rememberNavController(),
        CharacterDetailModel(
            id = "",
            name = "Mr. Preview",
            description = "This is a long description for the Android composable preview feature",
            pictureUrl = "",
            comics = listOf("Comic1", "Comic2", "Comic3"),
            series = listOf("Comic1", "Comic2", "Comic3"),
            stories = listOf("Comic1", "Comic2", "Comic3"),
            detailUrl = "",
            comicLinkUrl = "",
            wikiLinkUrl = "",
        )
    )
}
