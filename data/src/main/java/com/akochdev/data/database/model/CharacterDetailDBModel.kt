package com.akochdev.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "character_detail", indices = [Index("id")])
data class CharacterDetailDBModel(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val pictureUrl: String
)
