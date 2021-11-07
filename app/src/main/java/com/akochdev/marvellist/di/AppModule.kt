package com.akochdev.marvellist.di

import android.content.Context
import androidx.room.Room
import com.akochdev.data.database.MarvelDatabase
import com.akochdev.data.datasource.LocalDBDatasource
import com.akochdev.data.datasource.LocalDBDatasourceImpl
import com.akochdev.data.datasource.RemoteDataSource
import com.akochdev.data.datasource.RemoteDataSourceImpl
import com.akochdev.data.mapper.CharacterDatabaseMapperImpl
import com.akochdev.data.mapper.CharacterResponseMapperImpl
import com.akochdev.data.repository.MarvelRepositoryImpl
import com.akochdev.data.service.MARVEL_SERVICE_BASE_URL
import com.akochdev.data.service.MarvelService
import com.akochdev.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "baseUrl"
private const val DEFAULT_CONNECT_TIMEOUT = 5000L

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl() = MARVEL_SERVICE_BASE_URL.toHttpUrl()

    @Provides
    @Singleton
    fun provideRetrofit(@Named(BASE_URL) baseUrl: HttpUrl): Retrofit  {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
            .readTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
            .writeTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): MarvelService =
        retrofit.create(MarvelService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: MarvelService): RemoteDataSource =
        RemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideLocalDataSource(database: MarvelDatabase): LocalDBDatasource =
        LocalDBDatasourceImpl(database)


    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDBDatasource
    ): MarvelRepository {
        return MarvelRepositoryImpl(
            remoteDataSource,
            localDataSource,
            CharacterResponseMapperImpl(),
            CharacterDatabaseMapperImpl()
        )
    }

    @Provides
    @Singleton
    fun provideListUseCase(repository: MarvelRepository): CharacterListUseCase {
        return CharacterListUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideDetailUseCase(repository: MarvelRepository): CharacterDetailUseCase {
        return CharacterDetailUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MarvelDatabase {
        return Room.databaseBuilder(
            context,
            MarvelDatabase::class.java,
            MarvelDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
