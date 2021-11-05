package com.akochdev.data.datasource

import com.akochdev.data.database.MarvelDatabase
import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel
import javax.inject.Inject

interface LocalDBDatasource {
    suspend fun getMarvelCharacters(limit: Int, offset: Int): List<CharacterListItemDBModel>
    suspend fun insertMarvelCharacters(list: List<CharacterListItemDBModel>)
    suspend fun findCharacterDetail(characterId: String): CharacterDetailDBModel?
    suspend fun insertCharacterDetail(detail: CharacterDetailDBModel)
}

class LocalDBDatasourceImpl @Inject constructor(
    private val database: MarvelDatabase
) : LocalDBDatasource {
    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): List<CharacterListItemDBModel> {
        return database.characterListDao().findByPage(limit, offset)
    }

    override suspend fun insertMarvelCharacters(list: List<CharacterListItemDBModel>) {
        database.characterListDao().insert(list)
    }

    override suspend fun findCharacterDetail(characterId: String): CharacterDetailDBModel? {
        return database.characterDetailDao().findById(characterId).firstOrNull()
    }

    override suspend fun insertCharacterDetail(detail: CharacterDetailDBModel) {
        database.characterDetailDao().insert(detail)
    }
}
