package com.akochdev.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akochdev.data.database.model.CharacterDetailDBModel

@Dao
interface CharacterDetailDao {

    @Query("SELECT * from character_detail WHERE id = :characterId")
    suspend fun findById(characterId: String): List<CharacterDetailDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: CharacterDetailDBModel)
}
