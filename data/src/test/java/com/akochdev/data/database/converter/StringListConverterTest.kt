package com.akochdev.data.database.converter

import com.google.gson.Gson
import org.junit.Test
import org.junit.Assert.assertEquals

class StringListConverterTest {

    private val item1 = "item1"
    private val item2 = "item2"
    private val jsonTestData = "[$item1, $item2]"
    private val listTestData = listOf(item1, item2)

    private val converter = StringListConverter()

    @Test
    fun jsonToStringList() {
        val result = converter.jsonToStringList(jsonTestData)
        assertEquals(listTestData, result)
    }

    @Test
    fun stringListToJson() {
        val gson = Gson()
        val expected = gson.toJson(listTestData)
        val result = converter.stringListToJson(listTestData)
        assertEquals(expected, result)
    }
}