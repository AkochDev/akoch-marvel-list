package com.akochdev.marvellist.ui.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.marvellist.ui.compose.CharacterListScreenTestTags.LoadMoreItem
import com.akochdev.marvellist.ui.compose.CharacterListScreenTestTags.MainContainer
import com.akochdev.marvellist.ui.compose.CommonScreenTestTags.Loading
import com.akochdev.marvellist.ui.compose.CommonScreenTestTags.Error
import com.akochdev.marvellist.ui.state.CharacterListScreenUIState
import com.akochdev.marvellist.viewmodel.CharacterListViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class CharacterListScreenKtTest {

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<CharacterListViewModel>(relaxed = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalCoilApi
    @Test
    fun characterListScreenLoading() {
        val liveData = MutableLiveData(CharacterListScreenUIState(isLoading = true))
        every { viewModel.characterList } answers { liveData }

        composeTestRule.setContent {
            CharacterListScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Loading.tag).assertExists()
    }

    @ExperimentalCoilApi
    @Test
    fun characterListScreenEmpty() {
        val liveData = MutableLiveData(CharacterListScreenUIState(isEmptyState = true))
        every { viewModel.characterList } answers { liveData }

        composeTestRule.setContent {
            CharacterListScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Error.tag).assertExists()
    }

    @ExperimentalCoilApi
    @Test
    fun characterListScreenError() {
        val liveData = MutableLiveData(CharacterListScreenUIState(isErrorState = true))
        every { viewModel.characterList } answers { liveData }

        composeTestRule.setContent {
            CharacterListScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithTag(Error.tag).assertExists()
    }

    @Test
    fun contentCharacterListScreen() {
        val uiState = CharacterListScreenUIState(characterData = characterDatabaseModelList)
        composeTestRule.setContent {
            ContentCharacterListScreen(uiState, CharacterListViewModel.PAGE_SIZE, {}, {}, {})
        }
        composeTestRule.onNodeWithTag(MainContainer.tag).assertExists()
    }

    @Test
    fun contentCharacterListItem() {
        val item = characterDatabaseModelList.first()
        composeTestRule.setContent {
            ContentCharacterListItem(item) {}
        }
        composeTestRule.onNodeWithText(item.name).assertExists()
    }

    @Test
    fun contentLoadMoreCharacterListItem() {
        composeTestRule.setContent {
            ContentLoadMoreCharacterListItem {}
        }
        composeTestRule.onNodeWithTag(LoadMoreItem.tag).assertExists()
    }

    private val idDomain1 = "idDomain1"
    private val nameDomain1 = "nameDomain1"
    private val descriptionDomain1 = "descriptionDomain1"
    private val pictureUrlDomain1 = "pictureUrlDomain1"

    private val idDomain2 = "idDomain2"
    private val nameDomain2 = "nameDomain2"
    private val descriptionDomain2 = "descriptionDomain2"
    private val pictureUrlDomain2 = "pictureUrlDomain2"

    private val characterDatabaseModelList = listOf(
        CharacterListModel(
            id = idDomain1,
            name = nameDomain1,
            description = descriptionDomain1,
            pictureUrl = pictureUrlDomain1
        ),
        CharacterListModel(
            id = idDomain2,
            name = nameDomain2,
            description = descriptionDomain2,
            pictureUrl = pictureUrlDomain2
        )
    )
}