package com.akochdev.data.repository

import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Failure
import com.akochdev.domain.model.Result.Success
import com.akochdev.data.datasource.RemoteDataSource
import com.akochdev.data.mapper.CharacterResponseMapper
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.usecase.MarvelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor( // TODO add local datasource with room cache
    private val remoteDataSource: RemoteDataSource,
    private val mapper: CharacterResponseMapper
) : MarvelRepository {

    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): Result<List<CharacterListModel>> {
        return when (val result = remoteDataSource.getMarvelCharacters(limit, offset)) {
            is Success -> Success(mapper.toCharacterListDomain(result.value))
            is Failure -> Failure.fromFailure(result)
        }
    }

    override suspend fun getMarvelCharacterDetails(
        characterId: String
    ): Flow<Result<CharacterDetailModel>> {
        return remoteDataSource.getCharacterDetail(characterId).map { result ->
            when (result) {
                is Success -> Success(mapper.toCharacterDetailDomain(result.value))
                is Failure -> Failure.fromFailure(result)
            }
        }
    }
}
