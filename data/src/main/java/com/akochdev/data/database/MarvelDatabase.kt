package com.akochdev.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akochdev.data.database.dao.CharacterDetailDao
import com.akochdev.data.database.dao.CharacterListDao
import com.akochdev.data.database.model.CharacterDetailDBModel
import com.akochdev.data.database.model.CharacterListItemDBModel

@Database(
    entities = [
        CharacterDetailDBModel::class,
        CharacterListItemDBModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun characterListDao(): CharacterListDao
    abstract fun characterDetailDao(): CharacterDetailDao

    companion object {
        const val DATABASE_NAME = "marvel_database"
    }
}
