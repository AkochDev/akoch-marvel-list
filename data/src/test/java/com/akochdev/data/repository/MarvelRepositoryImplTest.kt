package com.akochdev.data.repository

import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel
import com.akochdev.data.datasource.LocalDBDatasource
import com.akochdev.data.datasource.RemoteDataSource
import com.akochdev.data.mapper.CharacterDatabaseMapper
import com.akochdev.data.mapper.CharacterResponseMapper
import com.akochdev.data.mapper.RESPONSE_URL_COMIC_JSON_KEY
import com.akochdev.data.mapper.RESPONSE_URL_DETAIL_JSON_KEY
import com.akochdev.data.mapper.RESPONSE_URL_WIKI_JSON_KEY
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
import com.akochdev.domain.model.Result
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.never
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MarvelRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = Mockito.mock(RemoteDataSource::class.java)
    private val localDataSource: LocalDBDatasource = Mockito.mock(LocalDBDatasource::class.java)
    private val responseMapper: CharacterResponseMapper =
        Mockito.mock(CharacterResponseMapper::class.java)
    private val databaseMapper: CharacterDatabaseMapper =
        Mockito.mock(CharacterDatabaseMapper::class.java)

    private val repository = MarvelRepositoryImpl(
        remoteDataSource,
        localDataSource,
        responseMapper,
        databaseMapper
    )

    @Test
    fun getMarvelCharactersNotCached() {
        runBlocking {
            val limit = 10
            val offset = 20
            whenever(localDataSource.getMarvelCharacters(limit, offset)).thenReturn(listOf())
            whenever(
                remoteDataSource.getMarvelCharacters(
                    any(),
                    any(),
                    any()
                )
            ).thenReturn(Result.Success<CharacterListResponseModel>(null))

            repository.getMarvelCharacters(limit, offset)

            verify(localDataSource).getMarvelCharacters(any(), any())
            verify(remoteDataSource).getMarvelCharacters(any(), any(), any())
            verify(responseMapper).toCharacterListDomain(anyOrNull())
            verify(localDataSource).insertMarvelCharacters(any())
            verify(databaseMapper).toCharacterListItemDb(any())
        }
    }

    @Test
    fun getMarvelCharactersCached() {
        runBlocking {
            val limit = 10
            val offset = 20
            whenever(localDataSource.getMarvelCharacters(limit, offset)).thenReturn(
                characterDatabaseModelList
            )

            repository.getMarvelCharacters(limit, offset)

            verify(localDataSource).getMarvelCharacters(any(), any())
            verify(remoteDataSource, never()).getMarvelCharacters(any(), any(), any())
            verify(responseMapper, never()).toCharacterListDomain(anyOrNull())
            verify(databaseMapper).toCharacterListDomain(characterDatabaseModelList)
        }
    }

    @Test
    fun getMarvelCharacterDetailsNotCached() {

        runBlocking {
            val characterId = "characterId"
            whenever(localDataSource.findCharacterDetail(anyOrNull())).thenReturn(null)
            whenever(responseMapper.toCharacterDetailDomain(anyOrNull())).thenReturn(characterDetailModel)
            whenever(
                remoteDataSource.getCharacterDetail(
                    any(),
                    any(),
                )
            ).thenReturn(
                flowOf(Result.Success(characterDetailResponse))
            )

            repository.getMarvelCharacterDetails(characterId).collect {  }

            verify(localDataSource).findCharacterDetail(any())
            verify(remoteDataSource).getCharacterDetail(any(), any())
            verify(responseMapper).toCharacterDetailDomain(anyOrNull())
            verify(localDataSource).insertCharacterDetail(anyOrNull())
            verify(databaseMapper).toCharacterDetailDb(anyOrNull())
        }
    }

    @Test
    fun getMarvelCharacterDetailsCached() {

        runBlocking {
            val characterId = "characterId"
            whenever(localDataSource.findCharacterDetail(anyOrNull())).thenReturn(characterDetailDb)

            repository.getMarvelCharacterDetails(characterId).collect {  }

            verify(localDataSource).findCharacterDetail(any())
            verify(remoteDataSource, never()).getCharacterDetail(any(), any())
            verify(responseMapper, never()).toCharacterDetailDomain(anyOrNull())
            verify(localDataSource, never()).insertCharacterDetail(anyOrNull())
            verify(databaseMapper, never()).toCharacterDetailDb(anyOrNull())
        }
    }

    private val idDomain1 = "idDomain1"
    private val nameDomain1 = "nameDomain1"
    private val descriptionDomain1 = "descriptionDomain1"
    private val pictureUrlDomain1 = "pictureUrlDomain1"

    private val idDomain2 = "idDomain2"
    private val nameDomain2 = "nameDomain2"
    private val descriptionDomain2 = "descriptionDomain2"
    private val pictureUrlDomain2 = "pictureUrlDomain2"

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

    private val pictureUrlDomainExtension = "jpg"

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
}
