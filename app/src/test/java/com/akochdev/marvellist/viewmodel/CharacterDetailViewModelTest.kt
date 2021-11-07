package com.akochdev.marvellist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.testing.TestLifecycleOwner
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.Result
import com.akochdev.domain.usecase.CharacterDetailUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val useCase = Mockito.mock(CharacterDetailUseCase::class.java)
    private val lifecycleObserver = TestLifecycleOwner()

    private val viewModel = CharacterDetailViewModel(useCase)

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun breakDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchCharacterDetailsSuccess() {
        runBlocking {
            val characterId = "characterId"

            whenever(useCase.getMarvelCharacterDetails(characterId)).doReturn(
                flow { emit(Result.Success(characterDetailModel)) }
            )

            viewModel.fetchCharacterDetails(characterId)
            viewModel.characterDetail.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isErrorState)
                assertFalse(uiState.isEmptyState)
                assertEquals(characterDetailModel, uiState.characterData)
            }
        }
    }

    @Test
    fun fetchCharacterDetailsEmptyResponse() {
        runBlocking {
            val characterId = "characterId"

            whenever(useCase.getMarvelCharacterDetails(characterId)).doReturn(
                flow { emit(Result.Success(null)) }
            )

            viewModel.fetchCharacterDetails(characterId)
            viewModel.characterDetail.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isErrorState)
                assertTrue(uiState.isEmptyState)
                assertNull(uiState.characterData)
            }
        }
    }

    @Test
    fun fetchCharacterDetailsError() {
        runBlocking {
            val characterId = "characterId"

            whenever(useCase.getMarvelCharacterDetails(characterId)).doReturn(
                flow { emit(Result.Failure()) }
            )

            viewModel.fetchCharacterDetails(characterId)
            viewModel.characterDetail.observe(lifecycleObserver) { uiState ->
                assertFalse(uiState.isLoading)
                assertTrue(uiState.isErrorState)
                assertFalse(uiState.isEmptyState)
                assertNull(uiState.characterData)
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
