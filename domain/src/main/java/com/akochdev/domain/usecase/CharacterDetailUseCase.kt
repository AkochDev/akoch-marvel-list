package com.akochdev.domain.usecase

import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterDetailUseCase @Inject constructor(
    private val repository: MarvelRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getMarvelCharacterDetails(
        characterId: String
    ): Flow<Result<CharacterDetailModel>> {
        return repository.getMarvelCharacterDetails(characterId).flowOn(dispatcher).conflate()
    }
}
