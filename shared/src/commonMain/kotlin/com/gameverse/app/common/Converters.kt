package com.gameverse.app.common

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(ListSerializer(Int.serializer()), value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return Json.decodeFromString(ListSerializer(Int.serializer()), value)
    }
}