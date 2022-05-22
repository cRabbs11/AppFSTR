package com.ekochkov.appfstr.data.entity

data class Mountain(
        val add_time: String,
        val beautyTitle: String,
        val connect: String,
        val coords: Coords,
        val id: Int,
        val images: List<Image>,
        val level: Level,
        val other_titles: String,
        val title: String,
        val type: String,
        val user: User
)