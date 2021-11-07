package com.akochdev.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterListUseCaseTest {

    private val repository: MarvelRepository = Mockito.mock(MarvelRepository::class.java)
    private val useCase = CharacterListUseCaseImpl(repository)

    @Test
    fun getMarvelCharacters() {
        val limit = 10
        val offset = 20
        runBlocking {
            useCase.getMarvelCharacters(limit, offset)
            Mockito.verify(repository).getMarvelCharacters(limit, offset)
        }
    }
}
