package com.ekochkov.appfstr.utils

import androidx.room.TypeConverter
import com.ekochkov.appfstr.data.entity.Image
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken




object ImagesTypeConverter {
    @TypeConverter
    fun fromImageToString(list: List<Image>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToImage(string: String): List<Image> {
        val array = Gson().fromJson(string, Array<Image>::class.java)
        return array.toList()
    }
}