package com.ekochkov.appfstr.utils

object CoordsConverter {

    fun fromDegreesToDecimal(degrees: Int, minutes: Int, seconds: Int): Double {
        var decimal = ((minutes.toDouble() * 60)+seconds.toDouble()) / (60*60)
        var answer = degrees.toDouble() + decimal
        return answer
    }

    fun fromDecimalToDegrees(value: Double): Array<Int> {
        val  degree = value.toInt()
        val rawMinute = Math.abs((value % 1) * 60)
        val minute = rawMinute.toInt()
        val second = (Math.round((rawMinute % 1) * 60)).toInt()
        return arrayOf(degree, minute, second)
    }
}