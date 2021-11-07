package com.akochdev.data.mapper

import com.akochdev.data.extensions.toSecureUrl
import com.akochdev.data.model.CharacterDetailResponseModel
import com.akochdev.domain.model.CharacterListModel
import com.akochdev.data.model.CharacterListResponseModel
import com.akochdev.domain.model.CharacterDetailModel

const val RESPONSE_URL_DETAIL_JSON_KEY = "detail"
const val RESPONSE_URL_COMIC_JSON_KEY = "comiclink"
const val RESPONSE_URL_WIKI_JSON_KEY = "wiki"

interface CharacterResponseMapper {
    fun toCharacterListDomain(response: CharacterListResponseModel?): List<CharacterListModel>?
    fun toCharacterDetailDomain(response: CharacterDetailResponseModel?): CharacterDetailModel?
}

class CharacterResponseMapperImpl : CharacterResponseMapper {

    override fun toCharacterListDomain(response: CharacterListResponseModel?): List<CharacterListModel>? {
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

    override fun toCharacterDetailDomain(response: CharacterDetailResponseModel?): CharacterDetailModel? {
        val responseVal = response?.data?.results?.firstOrNull() ?: return null
        with(responseVal) {
            return CharacterDetailModel(
                id = id,
                name = name,
                description = description,
                pictureUrl = "${thumbnail.path}.${thumbnail.extension}".toSecureUrl(),
                comics = comics.items.map { it.name },
                series = series.items.map { it.name },
                stories = stories.items.map { it.name },
                detailUrl = urls.firstOrNull { it.type == RESPONSE_URL_DETAIL_JSON_KEY }?.url?.toSecureUrl(),
                comicLinkUrl = urls.firstOrNull { it.type == RESPONSE_URL_COMIC_JSON_KEY }?.url?.toSecureUrl(),
                wikiLinkUrl = urls.firstOrNull { it.type == RESPONSE_URL_WIKI_JSON_KEY }?.url?.toSecureUrl(),
            )
        }
    }
}
