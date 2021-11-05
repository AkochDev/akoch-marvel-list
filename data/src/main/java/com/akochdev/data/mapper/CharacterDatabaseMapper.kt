package com.akochdev.data.mapper

import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.CharacterListModel

class CharacterDatabaseMapper {

    fun toCharacterListDomain(items: List<CharacterListItemDBModel>): List<CharacterListModel> {
        return items.map { item ->
            CharacterListModel(
                id = item.id,
                name = item.name,
                description = item.description,
                pictureUrl = item.pictureUrl
            )
        }
    }

    fun toCharacterDetailDomain(item: CharacterDetailDBModel): CharacterDetailModel {
        return CharacterDetailModel(
            id = item.id,
            name = item.name,
            description = item.description,
            pictureUrl = item.pictureUrl
        )
    }

    fun toCharacterListItemDb(items: List<CharacterListModel>): List<CharacterListItemDBModel> {
        return items.map { item ->
            CharacterListItemDBModel(
                id = item.id,
                name = item.name,
                description = item.description,
                pictureUrl = item.pictureUrl
            )
        }
    }

    fun toCharacterDetailDb(item: CharacterDetailModel): CharacterDetailDBModel {
        return CharacterDetailDBModel(
            id = item.id,
            name = item.name,
            description = item.description,
            pictureUrl = item.pictureUrl
        )
    }
}
