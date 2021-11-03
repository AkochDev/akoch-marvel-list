package com.akochdev.marvellist.di

import com.akochdev.data.datasource.RemoteDataSource
import com.akochdev.data.datasource.RemoteDataSourceImpl
import com.akochdev.data.mapper.CharacterListResponseMapper
import com.akochdev.data.repository.MarvelRepositoryImpl
import com.akochdev.data.service.MarvelService
import com.akochdev.domain.usecase.CharacterListUseCase
import com.akochdev.domain.usecase.MarvelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "baseUrl"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl() = "https://gateway.marvel.com".toHttpUrl()

    @Provides
    @Singleton
    fun provideRetrofit(@Named(BASE_URL) baseUrl: HttpUrl): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): MarvelService =
        retrofit.create(MarvelService::class.java)

    @Provides
    @Singleton
    fun provideDataSource(service: MarvelService): RemoteDataSource = RemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): MarvelRepository {
        return MarvelRepositoryImpl(remoteDataSource, CharacterListResponseMapper())
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: MarvelRepository): CharacterListUseCase {
        return CharacterListUseCase(repository)
    }
}
