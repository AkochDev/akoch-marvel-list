package com.akochdev.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterDetailUseCaseTest {

    private val characterId = "characterId"
    private val repository: MarvelRepository = Mockito.mock(MarvelRepository::class.java)
    private val useCase: CharacterDetailUseCase = CharacterDetailUseCase(repository)


    @Test
    fun getMarvelCharacterDetails() {
        runBlocking {
            useCase.getMarvelCharacterDetails(characterId)
            verify(repository).getMarvelCharacterDetails(characterId)
        }
    }
}
