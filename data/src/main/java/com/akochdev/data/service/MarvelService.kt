package com.akochdev.data.service

import com.akochdev.data.model.CharacterListResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("/v1/public/characters")
    suspend fun getCharacterList(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") timeStamp: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<CharacterListResponseModel>
}
