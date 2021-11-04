package com.akochdev.domain.usecase

import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface MarvelRepository {
    suspend fun getMarvelCharacters(limit: Int, offset: Int): Result<List<CharacterListModel>>
    suspend fun getMarvelCharacterDetails(characterId: String): Flow<Result<CharacterDetailModel>>
}
