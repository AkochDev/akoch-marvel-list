package com.akochdev.data.datasource

import com.akochdev.data.BuildConfig.MARVEL_API_KEY_PRIVATE
import com.akochdev.data.BuildConfig.MARVEL_API_KEY_PUBLIC
import com.akochdev.data.model.CharacterListResponseModel
import com.akochdev.domain.model.Result
import com.akochdev.domain.model.Result.Success
import com.akochdev.domain.model.Result.Failure
import com.akochdev.data.extensions.toMarvelHash
import com.akochdev.data.service.MarvelService
import java.lang.Exception
import java.util.Date

import javax.inject.Inject

private const val UNKNOWN_RESPONSE_CODE = -1

interface RemoteDataSource {
    suspend fun getMarvelCharacters(limit: Int, offset: Int): Result<CharacterListResponseModel>
}

class RemoteDataSourceImpl @Inject constructor(
    private val service: MarvelService
) : RemoteDataSource {

    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): Result<CharacterListResponseModel> {
        val timestamp = Date().time
        val hash = "$timestamp$MARVEL_API_KEY_PRIVATE$MARVEL_API_KEY_PUBLIC".toMarvelHash()
        return try {
            val response =
                service.getCharacterList(MARVEL_API_KEY_PUBLIC, hash, timestamp, limit, offset)
            if (response.isSuccessful) {
                Success(response.body())
            } else {
                Failure(response.code(), response.message())
            }
        } catch (e: Exception) {
            Failure(UNKNOWN_RESPONSE_CODE, e.localizedMessage, e)
        }
    }
}
