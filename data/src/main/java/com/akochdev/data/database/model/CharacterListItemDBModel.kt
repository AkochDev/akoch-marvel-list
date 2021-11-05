package com.akochdev.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_list")
data class CharacterListItemDBModel(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val pictureUrl: String
)
