package com.akochdev.data.mapper

import com.akochdev.data.extensions.toSecureUrl
import com.akochdev.data.model.CharacterDetailResponseModel
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.data.model.CharacterListResponseModel
import com.akochdev.domain.model.CharacterDetailModel

class CharacterResponseMapper {

    fun toCharacterListDomain(response: CharacterListResponseModel?): List<CharacterListModel>? {
        if (response == null) return null
        return response.data.results.map { item ->
            CharacterListModel(
                id = item.id,
                name = item.name,
                description = item.description,
                pictureUrl = "${item.thumbnail.path}.${item.thumbnail.extension}".toSecureUrl()
            )
        }
    }

    fun toCharacterDetailDomain(response: CharacterDetailResponseModel?): CharacterDetailModel? {
        val responseVal = response?.data?.results?.firstOrNull() ?: return null
        with(responseVal) {
            return CharacterDetailModel(
                id = id,
                name = name,
                description = description,
                pictureUrl = "${thumbnail.path}.${thumbnail.extension}".toSecureUrl()
            )
        }
    }
}
