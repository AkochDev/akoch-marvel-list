package com.akochdev.domain.usecase

import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import javax.inject.Inject

private const val NO_OFFSET = 0

interface CharacterListUseCase {
    suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int = NO_OFFSET
    ): Result<List<CharacterListModel>>
}

class CharacterListUseCaseImpl @Inject constructor(
    private val repository: MarvelRepository
) : CharacterListUseCase {

    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): Result<List<CharacterListModel>> {
        return repository.getMarvelCharacters(limit, offset)
    }
}
