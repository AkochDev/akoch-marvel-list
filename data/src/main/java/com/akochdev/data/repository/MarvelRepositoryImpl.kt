package com.akochdev.data.repository

import com.akochdev.data.datasource.LocalDBDatasource
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Failure
import com.akochdev.domain.model.Result.Success
import com.akochdev.data.datasource.RemoteDataSource
import com.akochdev.data.mapper.CharacterDatabaseMapper
import com.akochdev.data.mapper.CharacterResponseMapper
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.usecase.MarvelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDBDatasource,
    private val responseMapper: CharacterResponseMapper,
    private val databaseMapper: CharacterDatabaseMapper
) : MarvelRepository {

    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): Result<List<CharacterListModel>> {
        val localRecords = localDataSource.getMarvelCharacters(limit, offset)
        return if (localRecords.isNotEmpty()) {
            Success(databaseMapper.toCharacterListDomain(localRecords))
        } else {
            when (val result = remoteDataSource.getMarvelCharacters(limit, offset)) {
                is Success -> {
                    val newItems = responseMapper.toCharacterListDomain(result.value)
                    createLocalCache(newItems)
                    Success(newItems)
                }
                is Failure -> Failure.fromFailure(result)
                else -> Failure()
            }
        }
    }

    override suspend fun getMarvelCharacterDetails(
        characterId: String
    ): Flow<Result<CharacterDetailModel>> { // using flow just to showcase
        val character = localDataSource.findCharacterDetail(characterId)
        return if (character != null) {
            flow { emit(Success(databaseMapper.toCharacterDetailDomain(character))) }
        } else {
            remoteDataSource.getCharacterDetail(characterId).map { result ->
                when (result) {
                    is Success -> {
                        val newCharacter = responseMapper.toCharacterDetailDomain(result.value)
                        createLocalDetailCache(newCharacter)
                        Success(newCharacter)
                    }
                    is Failure -> Failure.fromFailure(result)
                }
            }
        }
    }

    private suspend fun createLocalCache(list: List<CharacterListModel>?) {
        list?.let { items ->
            localDataSource.insertMarvelCharacters(
                databaseMapper.toCharacterListItemDb(items)
            )
        }
    }

    private suspend fun createLocalDetailCache(item: CharacterDetailModel?) {
        item?.let { newChar ->
            localDataSource.insertCharacterDetail(
                databaseMapper.toCharacterDetailDb(newChar)
            )
        }
    }
}
