package com.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(services: List<String>): String {
        return gson.toJson(services)
    }

    @TypeConverter
    fun toList(json: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type

        return gson.fromJson<List<String>>(
            json,
            listType
        ).orEmpty()
    }
}