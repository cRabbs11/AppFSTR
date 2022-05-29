package com.ekochkov.appfstr.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    const val FORMAT_DD_MM_YYYY = "dd.MM.yyyy"

    fun fromLongToText(time: Long, pattern: String): String {
        val date = Date(time)
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun fromTextToLong(time: String, pattern: String): Long? {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return try {
            val date = dateFormat.parse(time)
            val timeLong = date?.time
            timeLong
        } catch (e: ParseException) {
            println("${e.printStackTrace()}")
            null
        }
    }
}