package com.akochdev.marvellist.ui.compose

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.marvellist.R
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.ComicLink
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.DetailLink
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.AppearancesTitle
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.Description
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.Name
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.Content
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.AppBarName
import com.akochdev.marvellist.ui.compose.CharacterDetailScreenTestTags.WikiLink
import com.akochdev.marvellist.ui.compose.CommonScreenTestTags.Error
import com.akochdev.marvellist.ui.compose.CommonScreenTestTags.Loading
import com.akochdev.marvellist.ui.state.CharacterDetailScreenUIState
import com.akochdev.marvellist.viewmodel.CharacterDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoilApi
@InternalCoroutinesApi
class CharacterDetailsScreenKtTest {

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<CharacterDetailViewModel>(relaxed = true)
    private val characterId = "characterId"

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun characterDetailScreenLoading() {
        val liveData = MutableLiveData(CharacterDetailScreenUIState(isLoading = true))
        every { viewModel.characterDetail } answers { liveData }

        composeTestRule.setContent {
            CharacterDetailScreen(characterId, navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Loading.tag).assertExists()
    }

    /**
     * For some reason this test fail when we launch all the test at once, but pass when launching it individually...
     */
    @Test
    fun characterDetailScreenError() {
        val liveData = MutableLiveData(CharacterDetailScreenUIState(isErrorState = true))
        every { viewModel.characterDetail } answers { liveData }

        composeTestRule.setContent {
            CharacterDetailScreen(characterId, navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Error.tag).assertExists()
    }

    @Test
    fun characterDetailScreenEmpty() {
        val liveData = MutableLiveData(CharacterDetailScreenUIState(isEmptyState = true))
        every { viewModel.characterDetail } answers { liveData }

        composeTestRule.setContent {
            CharacterDetailScreen(characterId, navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Error.tag).assertExists()
    }

    @Test
    fun characterDetailScreenSuccess() {
        val liveData =
            MutableLiveData(CharacterDetailScreenUIState(characterData = characterDetailModel))
        every { viewModel.characterDetail } answers { liveData }

        composeTestRule.setContent {
            CharacterDetailScreen(characterId, navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Content.tag).assertExists()
    }

    @Test
    fun contentCharacterDetail() {
        var goBackText: String? = null
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel)
            goBackText = stringResource(id = R.string.image_go_back_arrow)
        }

        with(composeTestRule) {
            onNodeWithContentDescription(goBackText!!).performClick()
            verify { navController.popBackStack() }

            onNodeWithTag(AppBarName.tag).apply {
                assertExists()
                assertTextEquals(characterDetailModel.name)
            }

            onNodeWithTag(Name.tag).apply {
                assertExists()
                assertTextEquals(characterDetailModel.name)
            }

            onNodeWithTag(Description.tag).apply {
                assertExists()
                assertTextEquals(characterDetailModel.description)
            }
        }
    }

    @Test
    fun contentCharacterDetailNoDescription() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel.copy(description = ""))
        }

        composeTestRule.onNodeWithTag(Description.tag).apply {
            assertDoesNotExist()
        }
    }

    @Test
    fun characterDetailsAppearances() {
        var appearancesTitle: String? = null
        var appearancesComicsTitle: String? = null
        var appearancesSeriesTitle: String? = null
        var appearancesStoriesTitle: String? = null

        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel)
            appearancesTitle = stringResource(id = R.string.text_character_detail_participated_on)
            appearancesComicsTitle = stringResource(id = R.string.text_character_detail_comics)
            appearancesSeriesTitle = stringResource(id = R.string.text_character_detail_series)
            appearancesStoriesTitle = stringResource(id = R.string.text_character_detail_stories)
        }
        with(composeTestRule) {
            onNodeWithTag(AppearancesTitle.tag).apply {
                assertExists()
                assertTextEquals(appearancesTitle!!)
            }
            onNodeWithText(appearancesComicsTitle!!).apply {
                assertExists()
            }
            onNodeWithText(appearancesSeriesTitle!!).apply {
                assertExists()
            }
            onNodeWithText(appearancesStoriesTitle!!).apply {
                assertExists()
            }
        }
    }

    @Test
    fun characterDetailsAppearancesNone() {
        var appearancesComicsTitle: String? = null
        var appearancesSeriesTitle: String? = null
        var appearancesStoriesTitle: String? = null

        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    comics = listOf(),
                    stories = listOf(),
                    series = listOf()
                )
            )

            appearancesComicsTitle = stringResource(id = R.string.text_character_detail_comics)
            appearancesSeriesTitle = stringResource(id = R.string.text_character_detail_series)
            appearancesStoriesTitle = stringResource(id = R.string.text_character_detail_stories)
        }
        with(composeTestRule) {
            onNodeWithTag(AppearancesTitle.tag).apply {
                assertDoesNotExist()
            }
            onNodeWithText(appearancesComicsTitle!!).apply {
                assertDoesNotExist()
            }
            onNodeWithText(appearancesSeriesTitle!!).apply {
                assertDoesNotExist()
            }
            onNodeWithText(appearancesStoriesTitle!!).apply {
                assertDoesNotExist()
            }
        }
    }

    @Test
    fun characterDetailsAppearancesComics() {
        var appearancesComicsTitle: String? = null

        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    stories = listOf(),
                    series = listOf()
                )
            )

            appearancesComicsTitle = stringResource(id = R.string.text_character_detail_comics)

        }
        with(composeTestRule) {
            onNodeWithTag(AppearancesTitle.tag).apply {
                assertExists()
            }
            onNodeWithText(appearancesComicsTitle!!).apply {
                assertExists()
            }
        }
    }

    @Test
    fun characterDetailsAppearancesStories() {
        var appearancesStoriesTitle: String? = null

        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    comics = listOf(),
                    series = listOf()
                )
            )

            appearancesStoriesTitle = stringResource(id = R.string.text_character_detail_stories)

        }
        with(composeTestRule) {
            onNodeWithTag(AppearancesTitle.tag).apply {
                assertExists()
            }
            onNodeWithText(appearancesStoriesTitle!!).apply {
                assertExists()
            }
        }
    }

    @Test
    fun characterDetailsAppearancesSeries() {
        var appearancesSeriesTitle: String? = null

        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    stories = listOf(),
                    comics = listOf()
                )
            )

            appearancesSeriesTitle = stringResource(id = R.string.text_character_detail_series)

        }
        with(composeTestRule) {
            onNodeWithTag(AppearancesTitle.tag).apply {
                assertExists()
            }
            onNodeWithText(appearancesSeriesTitle!!).apply {
                assertExists()
            }
        }
    }

    @Test
    fun visitLinkWikiExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel)
        }
        with(composeTestRule) {
            onNodeWithTag(WikiLink.tag).apply {
                assertExists()
            }
        }
    }

    @Test
    fun visitLinkWikiNotExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    wikiLinkUrl = null
                )
            )
        }
        with(composeTestRule) {
            onNodeWithTag(WikiLink.tag).apply {
                assertDoesNotExist()
            }
        }
    }

    @Test
    fun visitLinkComicExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel)
        }
        with(composeTestRule) {
            onNodeWithTag(WikiLink.tag).apply {
                assertExists()
            }
        }
    }

    @Test
    fun visitLinkComicNotExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    comicLinkUrl = null
                )
            )
        }
        with(composeTestRule) {
            onNodeWithTag(ComicLink.tag).apply {
                assertDoesNotExist()
            }
        }
    }

    @Test
    fun visitLinkDetailExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController, characterDetailModel)
        }
        with(composeTestRule) {
            onNodeWithTag(DetailLink.tag).apply {
                assertExists()
            }
        }
    }

    @Test
    fun visitLinkDetailNotExists() {
        composeTestRule.setContent {
            ContentCharacterDetail(navController = navController,
                characterDetailModel.copy(
                    detailUrl = null
                )
            )
        }
        with(composeTestRule) {
            onNodeWithTag(DetailLink.tag).apply {
                assertDoesNotExist()
            }
        }
    }

    private val idDetailDomain = "idDetailDomain"
    private val nameDetailDomain = "nameDetailDomain"
    private val descriptionDetailDomain = "descriptionDetailDomain"
    private val pictureUrlDetailDomain = "pictureUrlDetailDomain"
    private val comicsDetailDomain = listOf("comicsDetailDomain")
    private val seriesDetailDomain = listOf("seriesDetailDomain")
    private val storiesDetailDomain = listOf("storiesDetailDomain")
    private val detailUrlDetailDomain = "detailUrlDetailDomain"
    private val comicLinkDetailDomain = "comicLinkDetailDomain"
    private val wikiLinkDetailDomain = "wikiLinkDetailDomain"

    private val characterDetailModel = CharacterDetailModel(
        id = idDetailDomain,
        name = nameDetailDomain,
        description = descriptionDetailDomain,
        pictureUrl = pictureUrlDetailDomain,
        comics = comicsDetailDomain,
        series = seriesDetailDomain,
        stories = storiesDetailDomain,
        detailUrl = detailUrlDetailDomain,
        comicLinkUrl = comicLinkDetailDomain,
        wikiLinkUrl = wikiLinkDetailDomain
    )
}