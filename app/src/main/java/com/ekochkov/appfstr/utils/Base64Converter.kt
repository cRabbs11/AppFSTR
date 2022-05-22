package com.ekochkov.appfstr.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object Base64Converter {

    const val COMPRESS_FULL_QUALITY = 100

    fun fromBase64ToBitmap(encodedImage: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun fromBitmapToBase64(bitmap: Bitmap): String {
        val stream= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_FULL_QUALITY,stream)
        val  bytes=stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}