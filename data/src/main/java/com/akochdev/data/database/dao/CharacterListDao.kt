package com.akochdev.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akochdev.data.database.model.CharacterListItemDBModel

@Dao
interface CharacterListDao {

    @Query("SELECT * from character_list ORDER BY name LIMIT :limit OFFSET :offset")
    suspend fun findByPage(limit: Int, offset: Int): List<CharacterListItemDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CharacterListItemDBModel>)
}
