package com.akochdev.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class StringListConverter {

    private val gson: Gson = Gson()

    @TypeConverter
    fun jsonToStringList(data: String?): List<String?>? {
        if (data == null) {
            return emptyList()
        }
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun stringListToJson(someObjects: List<String?>?): String? = gson.toJson(someObjects)
}
