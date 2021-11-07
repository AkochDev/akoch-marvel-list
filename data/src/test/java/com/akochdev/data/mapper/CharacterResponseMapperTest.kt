package com.akochdev.data.mapper

import com.akochdev.data.model.CharacterDetailResponseModel
import com.akochdev.data.model.CharacterListResponseModel
import com.akochdev.data.model.Comics
import com.akochdev.data.model.Data
import com.akochdev.data.model.Events
import com.akochdev.data.model.Items
import com.akochdev.data.model.Results
import com.akochdev.data.model.Series
import com.akochdev.data.model.Stories
import com.akochdev.data.model.Thumbnail
import com.akochdev.data.model.Urls
import com.akochdev.domain.model.CharacterDetailModel
import com.akochdev.domain.model.CharacterListModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CharacterResponseMapperTest {

    private val mapper = CharacterResponseMapperImpl()

    @Test
    fun toCharacterListDomain() {
        val result = mapper.toCharacterListDomain(characterListResponse)
        assertEquals(characterDomainModelList, result)
    }

    @Test
    fun toCharacterDetailDomain() {
        val result = mapper.toCharacterDetailDomain(characterDetailResponse)
        assertEquals(characterDetailModel, result)
    }


    private val idDomain1 = "idDomain1"
    private val nameDomain1 = "nameDomain1"
    private val descriptionDomain1 = "descriptionDomain1"
    private val pictureUrlDomain1 = "pictureUrlDomain1"

    private val idDomain2 = "idDomain2"
    private val nameDomain2 = "nameDomain2"
    private val descriptionDomain2 = "descriptionDomain2"
    private val pictureUrlDomain2 = "pictureUrlDomain2"
    private val pictureUrlDomainExtension = "jpg"

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
            pictureUrl = "$pictureUrlDomain1.$pictureUrlDomainExtension"
        ),
        CharacterListModel(
            id = idDomain2,
            name = nameDomain2,
            description = descriptionDomain2,
            pictureUrl = "$pictureUrlDomain2.$pictureUrlDomainExtension"
        )
    )

    private val characterDetailModel = CharacterDetailModel(
        id = idDetailDomain,
        name = nameDetailDomain,
        description = descriptionDetailDomain,
        pictureUrl = "$pictureUrlDetailDomain.$pictureUrlDomainExtension",
        comics = comicsDetailDomain,
        series = seriesDetailDomain,
        stories = storiesDetailDomain,
        detailUrl = detailUrlDetailDomain,
        comicLinkUrl = comicLinkDetailDomain,
        wikiLinkUrl = wikiLinkDetailDomain
    )

    private val characterListResponseResult1 = Results(
        id = idDomain1,
        name = nameDomain1,
        description = descriptionDomain1,
        modified = "",
        resourceURI = "",
        urls = listOf(),
        thumbnail = Thumbnail(pictureUrlDomain1, pictureUrlDomainExtension),
        comics = Comics("", "", "", listOf()),
        stories = Stories("", "", "", listOf()),
        events = Events("", "", "", listOf()),
        series = Series("", "", "", listOf())
    )

    private val characterListResponseResult2 = Results(
        id = idDomain2,
        name = nameDomain2,
        description = descriptionDomain2,
        modified = "",
        resourceURI = "",
        urls = listOf(),
        thumbnail = Thumbnail(pictureUrlDomain2, pictureUrlDomainExtension),
        comics = Comics("", "", "", listOf()),
        stories = Stories("", "", "", listOf()),
        events = Events("", "", "", listOf()),
        series = Series("", "", "", listOf())
    )

    private val characterListResponseData = Data(
        offset = "",
        limit = "",
        total = "",
        count = "",
        results = listOf(characterListResponseResult1, characterListResponseResult2),
    )

    private val characterListResponse = CharacterListResponseModel(
        code = "",
        status = "",
        copyright = "",
        attributionText = "",
        attributionHTML = "",
        data = characterListResponseData,
        etag = ""
    )

    private val characterDetailResponseResult = Results(
        id = idDetailDomain,
        name = nameDetailDomain,
        description = descriptionDetailDomain,
        modified = "",
        resourceURI = "",
        urls = listOf(
            Urls(type = RESPONSE_URL_COMIC_JSON_KEY, url = comicLinkDetailDomain),
            Urls(type = RESPONSE_URL_DETAIL_JSON_KEY, url = detailUrlDetailDomain),
            Urls(type = RESPONSE_URL_WIKI_JSON_KEY, url = wikiLinkDetailDomain)
        ),
        thumbnail = Thumbnail(pictureUrlDetailDomain, pictureUrlDomainExtension),
        comics = Comics(
            "", "", "", listOf(
                Items(
                    resourceURI = "",
                    name = comicsDetailDomain.first(),
                    type = ""
                )
            )
        ),
        stories = Stories(
            "", "", "", listOf(
                Items(
                    resourceURI = "",
                    name = storiesDetailDomain.first(),
                    type = ""
                )
            )
        ),
        events = Events("", "", "", listOf()),
        series = Series(
            "", "", "", listOf(
                Items(
                    resourceURI = "",
                    name = seriesDetailDomain.first(),
                    type = ""
                )
            )
        )
    )

    private val characterDetailResponseData = Data(
        offset = "",
        limit = "",
        total = "",
        count = "",
        results = listOf(characterDetailResponseResult),
    )

    private val characterDetailResponse = CharacterDetailResponseModel(
        code = 1,
        status = "",
        copyright = "",
        attributionText = "",
        attributionHTML = "",
        data = characterDetailResponseData,
        etag = ""
    )
}
