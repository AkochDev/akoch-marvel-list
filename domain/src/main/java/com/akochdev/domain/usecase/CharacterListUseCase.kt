package com.akochdev.domain.usecase

import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import javax.inject.Inject

class CharacterListUseCase @Inject constructor(private val repository: MarvelRepository) {

    suspend fun getMarvelCharacters(limit: Int, offset: Int): Result<CharacterListModel> {
        return repository.getMarvelCharacters(limit, offset)
    }
}
