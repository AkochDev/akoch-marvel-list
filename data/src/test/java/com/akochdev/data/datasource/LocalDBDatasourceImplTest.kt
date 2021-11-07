package com.akochdev.data.datasource

import com.akochdev.data.database.MarvelDatabase
import com.akochdev.data.database.dao.CharacterDetailDao
import com.akochdev.data.database.dao.CharacterListDao
import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LocalDBDatasourceImplTest {

    private val database: MarvelDatabase = Mockito.mock(MarvelDatabase::class.java)
    private val characterListDao: CharacterListDao = Mockito.mock(CharacterListDao::class.java)
    private val characterDetailDao: CharacterDetailDao =
        Mockito.mock(CharacterDetailDao::class.java)

    private val dataSource = LocalDBDatasourceImpl(database)

    @Before
    fun setUp() {
        database.stub {
            on { characterListDao() } doReturn characterListDao
            on { characterDetailDao() } doReturn characterDetailDao
        }
    }

    @Test
    fun getMarvelCharacters() {
        runBlocking {
            val limit = 10
            val offset = 20
            dataSource.getMarvelCharacters(limit, offset)
            verify(characterListDao).findByPage(limit, offset)
        }
    }

    @Test
    fun insertMarvelCharacters() {
        runBlocking {
            val list = listOf(CharacterListItemDBModel("1", "name", "desc", "url"))
            dataSource.insertMarvelCharacters(list)
            verify(characterListDao).insert(list)
        }
    }

    @Test
    fun findCharacterDetail() {
        runBlocking {
            val characterId = "characterId"
            whenever(characterDetailDao.findById(characterId)).thenReturn(listOf())
            dataSource.findCharacterDetail(characterId)
            verify(characterDetailDao).findById(characterId)
        }
    }

    @Test
    fun insertCharacterDetail() {
        runBlocking {
            val item = mockk<CharacterDetailDBModel>()
            dataSource.insertCharacterDetail(item)
            verify(characterDetailDao).insert(item)
        }
    }
}
