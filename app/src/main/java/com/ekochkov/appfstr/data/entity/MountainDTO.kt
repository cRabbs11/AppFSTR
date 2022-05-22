package com.ekochkov.appfstr.data.entity

data class MountainDTO(
    val add_time: String,
    val beautyTitle: String,
    val connect: String,
    val coords: CoordsDTO,
    val id: Int,
    val images: List<ImageDTO>,
    val level: LevelDTO,
    val other_titles: String,
    val title: String,
    val type: String,
    val user: UserDTO
)