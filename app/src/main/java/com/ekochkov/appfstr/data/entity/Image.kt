package com.ekochkov.appfstr.data.entity

data class Image (
    var title: String?,
    val url: String?
    ) {

    companion object {
        const val DEFAULT_TITLE = ""
    }
}
