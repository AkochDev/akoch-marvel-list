package com.akochdev.data.mapper

import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.CharacterListModel
import org.junit.Test

import org.junit.Assert.assertEquals

class CharacterDatabaseMapperTest {

    private val mapper = CharacterDatabaseMapperImpl()

    @Test
    fun toCharacterListDomain() {
        val result = mapper.toCharacterListDomain(characterDatabaseModelList)
        assertEquals(characterDomainModelList, result)
    }

    @Test
    fun toCharacterDetailDomain() {
        val result = mapper.toCharacterDetailDomain(characterDetailDb)
        assertEquals(characterDetailModel, (result))
    }

    @Test
    fun toCharacterListItemDb() {
        val result = mapper.toCharacterListItemDb(characterDomainModelList)
        assertEquals(characterDatabaseModelList, result)
    }

    @Test
    fun toCharacterDetailDb() {
        val result = mapper.toCharacterDetailDb(characterDetailModel)
        assertEquals(characterDetailDb, result)
    }


    private val idDomain1 = "idDomain1"
    private val nameDomain1 = "nameDomain1"
    private val descriptionDomain1 = "descriptionDomain1"
    private val pictureUrlDomain1 = "pictureUrlDomain1"

    private val idDomain2 = "idDomain2"
    private val nameDomain2 = "nameDomain2"
    private val descriptionDomain2 = "descriptionDomain2"
    private val pictureUrlDomain2 = "pictureUrlDomain2"

    private val idDetailDomain = "idDetailDomain"
    private val nameDetailDomain = "nameDetailDomain"
    private val descriptionDetailDomain = "descriptionDetailDomain"
    private val pictureUrlDetailDomain = "pictureUrlDetailDomain"
    private val comicsDetailDomain = listOf("comicsDetailDomain")
    private val seriesDetailDomain = listOf("seriesDetailDomain")
    private val storiesDetailDomain = listOf("storiesDetailDomain")
    private val detailUrlDetailDomain = "detailUrlDetailDomain"
    private val comicLinkDetailDomain = "comicLinkDetailDomain"
    private val wikiLinkDetailDomain = "wikiLinkDetailDomain"

    private val characterDomainModelList = listOf(
        CharacterListModel(
            id = idDomain1,
            name = nameDomain1,
            description = descriptionDomain1,
            pictureUrl = pictureUrlDomain1
        ),
        CharacterListModel(
            id = idDomain2,
            name = nameDomain2,
            description = descriptionDomain2,
            pictureUrl = pictureUrlDomain2
        )
    )

    private val characterDatabaseModelList = listOf(
        CharacterListItemDBModel(
            id = idDomain1,
            name = nameDomain1,
            description = descriptionDomain1,
            pictureUrl = pictureUrlDomain1
        ),
        CharacterListItemDBModel(
            id = idDomain2,
            name = nameDomain2,
            description = descriptionDomain2,
            pictureUrl = pictureUrlDomain2
        )
    )

    private val characterDetailDb = CharacterDetailDBModel(
        id = idDetailDomain,
        name = nameDetailDomain,
        description = descriptionDetailDomain,
        pictureUrl = pictureUrlDetailDomain,
        comics = comicsDetailDomain,
        series = seriesDetailDomain,
        stories = storiesDetailDomain,
        detailUrl = detailUrlDetailDomain,
        comicLinkUrl = comicLinkDetailDomain,
        wikiLinkUrl = wikiLinkDetailDomain
    )

    private val characterDetailModel = CharacterDetailModel(
        id = idDetailDomain,
        name = nameDetailDomain,
        description = descriptionDetailDomain,
        pictureUrl = pictureUrlDetailDomain,
        comics = comicsDetailDomain,
        series = seriesDetailDomain,
        stories = storiesDetailDomain,
        detailUrl = detailUrlDetailDomain,
        comicLinkUrl = comicLinkDetailDomain,
        wikiLinkUrl = wikiLinkDetailDomain
    )
}
