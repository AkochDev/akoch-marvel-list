package com.akochdev.data.mapper

import com.akochdev.domain.model.CharacterListModel
import com.akochdev.data.model.CharacterListResponseModel

class CharacterListResponseMapper {

    suspend fun toCharacterListDomain(response: CharacterListResponseModel?): CharacterListModel? {
        if (response == null) return null
        return CharacterListModel(response.data.results.map { it.name })
    }
}
