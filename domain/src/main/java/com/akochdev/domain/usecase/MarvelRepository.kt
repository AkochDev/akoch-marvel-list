package com.akochdev.domain.usecase

import com.akochdev.domain.model.CharacterListModel
import com.akochdev.domain.model.Result

interface MarvelRepository {
    suspend fun getMarvelCharacters(limit: Int, offset: Int): Result<CharacterListModel>
}
