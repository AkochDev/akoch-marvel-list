package com.akochdev.marvellist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.testing.TestLifecycleOwner
import com.akochdev.data.database.model.CharacterListItemDBModel
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import com.akochdev.domain.usecase.CharacterListUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private val useCase = Mockito.mock(CharacterListUseCase::class.java)
    private val lifecycleObserver = TestLifecycleOwner()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun breakDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchCharacterListSuccess() {
        runBlocking(Dispatchers.Main) {
            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(
                Result.Success(
                    characterDatabaseModelList
                ) as Result<List<CharacterListModel>>
            )

            val viewModel = CharacterListViewModel(useCase) // The ViewModel calls fetchCharacterList() on the init{} block

            viewModel.characterList.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isErrorState)
                assertFalse(uiState.isEmptyState)
                assertFalse(uiState.characterData.isNullOrEmpty())
                val characterData = uiState.characterData
                assertEquals(characterDatabaseModelList, characterData)
            }
        }
    }

    @Test
    fun fetchCharacterListSuccessWithEmptyResponse() {
        runBlocking(Dispatchers.Main) {
            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(Result.Success(null))

            val viewModel = CharacterListViewModel(useCase)

            viewModel.characterList.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isErrorState)
                assertTrue(uiState.isEmptyState)
                assertTrue(uiState.characterData == null)
            }

        }
    }

    @Test
    fun fetchCharacterListFailure() {
        runBlocking(Dispatchers.Main) {
            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(Result.Failure())

            val viewModel = CharacterListViewModel(useCase)

            viewModel.characterList.observe(lifecycleObserver) { uiState ->
                assertTrue(uiState.isErrorState)
                assertFalse(uiState.isEmptyState)
                assertTrue(uiState.characterData.isNullOrEmpty())
            }
        }
    }

    @Test
    fun loadMoreSuccess() {
        runBlocking(Dispatchers.Main) {
            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(
                Result.Success(
                    characterDatabaseModelList
                ) as Result<List<CharacterListModel>>
            )

            val viewModel = CharacterListViewModel(useCase)
            viewModel.loadMore(forced = true)
            viewModel.characterList.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isErrorState)
                assertFalse(uiState.isEmptyState)
                assertFalse(uiState.characterData.isNullOrEmpty())
                assertEquals(characterDatabaseModelList.plus(characterDatabaseModelList), uiState.characterData)
            }
        }
    }

    /**
     * This test is tricky because of the nature of the Compose UI framework we do emit several values
     * for the UIState while the method is executed (this to reflect the "loading" state on the UI).
     * This is why we have an "if" within the "observe" method
     */

    @Test
    fun loadMoreFailure() {
        runBlocking(Dispatchers.Main) {
            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(
                Result.Success(
                    characterDatabaseModelList
                ) as Result<List<CharacterListModel>>
            )

            val viewModel = CharacterListViewModel(useCase)

            whenever(useCase.getMarvelCharacters(any(), any())).thenReturn(
                Result.Failure()
            )

            viewModel.loadMore(true)

            viewModel.characterList.observe(lifecycleObserver) { uiState ->
                if (uiState.isLoading) { // read the method's header for explanation on this "if"
                    assertFalse(uiState.isErrorState)
                    assertFalse(uiState.isEmptyState)
                    assertFalse(uiState.isErrorLoadingMore)
                    assertEquals(characterDatabaseModelList, uiState.characterData)
                } else {
                    assertFalse(uiState.isErrorState)
                    assertFalse(uiState.isEmptyState)
                    assertTrue(uiState.isErrorLoadingMore)
                    assertEquals(characterDatabaseModelList, uiState.characterData)
                }
            }
        }
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
        CharacterListItemDBModel(
            id = idDomain1,
            name = nameDomain1,
            description = descriptionDomain1,
            pictureUrl = pictureUrlDomain1
        ),
        CharacterListItemDBModel(
            id = idDomain2,
            name = nameDomain2,
            description = descriptionDomain2,
            pictureUrl = pictureUrlDomain2
        )
    )
}
