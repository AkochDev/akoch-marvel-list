package com.akochdev.data.datasource

import com.akochdev.data.BuildConfig
import com.akochdev.data.extensions.toMarvelHash
import com.akochdev.data.model.CharacterDetailResponseModel
import com.akochdev.data.service.MarvelService
import com.akochdev.domain.model.Result
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.internal.http.RealResponseBody
import okio.BufferedSource
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceImplTest {

    private val service = Mockito.mock(MarvelService::class.java)
    private val datasource = RemoteDataSourceImpl(service)

    @Test
    fun getMarvelCharactersSuccess() {
        runBlocking {
            val limit = 10
            val offset = 20
            val timestamp = 1L
            val hash = getHash(timestamp)

            whenever(
                service.getCharacterList(
                    BuildConfig.MARVEL_API_KEY_PUBLIC,
                    hash,
                    timestamp,
                    limit,
                    offset
                )
            ).thenReturn(
                Response.success(null)
            )

            val result = datasource.getMarvelCharacters(limit, offset, timestamp)

            verify(service).getCharacterList(
                BuildConfig.MARVEL_API_KEY_PUBLIC,
                hash,
                timestamp,
                limit,
                offset
            )
            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun getMarvelCharactersFailure() {
        runBlocking {
            val limit = 10
            val offset = 20
            val timestamp = 1L
            val hash = getHash(timestamp)
            val source: BufferedSource = Mockito.mock(BufferedSource::class.java)

            whenever(
                service.getCharacterList(
                    BuildConfig.MARVEL_API_KEY_PUBLIC,
                    hash,
                    timestamp,
                    limit,
                    offset
                )
            ).thenReturn(
                Response.error(400, RealResponseBody("", 1L, source))
            )

            val result = datasource.getMarvelCharacters(limit, offset, timestamp)

            verify(service).getCharacterList(
                BuildConfig.MARVEL_API_KEY_PUBLIC,
                hash,
                timestamp,
                limit,
                offset
            )
            assertTrue(result is Result.Failure)
        }
    }

    @Test
    fun getCharacterDetailSuccess() {
        runBlocking {
            val characterId = "characterId"
            val timestamp = 1L
            val hash = getHash(timestamp)

            whenever(
                service.getCharacterDetail(
                    characterId,
                    BuildConfig.MARVEL_API_KEY_PUBLIC,
                    hash,
                    timestamp
                )
            ).thenReturn(
                Response.success(null)
            )

            var result : Result<CharacterDetailResponseModel>? = null
            datasource.getCharacterDetail(characterId, timestamp).collect {
                result = it
            }

            verify(service).getCharacterDetail(
                characterId,
                BuildConfig.MARVEL_API_KEY_PUBLIC,
                hash,
                timestamp
            )
            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun getCharacterDetailError() {
        runBlocking {
            val characterId = "characterId"
            val timestamp = 1L
            val hash = getHash(timestamp)
            val source: BufferedSource = Mockito.mock(BufferedSource::class.java)
            whenever(
                service.getCharacterDetail(
                    characterId,
                    BuildConfig.MARVEL_API_KEY_PUBLIC,
                    hash,
                    timestamp
                )
            ).thenReturn(
                Response.error(400, RealResponseBody("", 1L, source))
            )
            var result : Result<CharacterDetailResponseModel>? = null
            datasource.getCharacterDetail(characterId, timestamp).collect {
                result = it
            }
            verify(service).getCharacterDetail(
                characterId,
                BuildConfig.MARVEL_API_KEY_PUBLIC,
                hash,
                timestamp
            )
            assertTrue(result is Result.Failure)
        }
    }

    private fun getHash(timestamp: Long) =
        "$timestamp${BuildConfig.MARVEL_API_KEY_PRIVATE}${BuildConfig.MARVEL_API_KEY_PUBLIC}".toMarvelHash()
}
