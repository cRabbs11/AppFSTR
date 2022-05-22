package com.ekochkov.appfstr.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.ekochkov.appfstr.data.entity.*
import java.io.ByteArrayOutputStream


object Converter {

    fun fromMountainDTOToMountain(mountainDTO: MountainDTO): Mountain {
        val images = arrayListOf<Image>()
        mountainDTO.images.forEach {
            images.add(fromImageDTOToImage(it))
        }

        return Mountain(
            add_time = mountainDTO.add_time,
            beautyTitle = mountainDTO.beautyTitle,
            connect = mountainDTO.connect,
            coords = fromCooredsDTOToCoords(mountainDTO.coords),
            id = mountainDTO.id,
            images = images,
            level = fromLevelDTOToLevel(mountainDTO.level),
            other_titles = mountainDTO.other_titles,
            title = mountainDTO.title,
            type = mountainDTO.type,
            user = fromUserDTOToUser(mountainDTO.user)
        )
    }

    private fun fromCooredsDTOToCoords(coordsDTO: CoordsDTO): Coords {
        return Coords(
            height = coordsDTO.height,
            latitude = coordsDTO.latitude,
            longitude = coordsDTO.longitude
        )
    }

    private fun fromImageDTOToImage(imageDTO: ImageDTO): Image {
        return Image(
            title = imageDTO.title,
            url = imageDTO.url
        )
    }

    private fun fromLevelDTOToLevel(levelDTO: LevelDTO): Level {
        return Level(
            autumn = levelDTO.autumn,
            spring = levelDTO.spring,
            summer = levelDTO.summer,
            winter = levelDTO.winter
        )
    }

    private fun fromUserDTOToUser(userDTO: UserDTO): User {
        return User(
            id = userDTO.id,
            name = userDTO.name,
            secondName = userDTO.fam,
            email = userDTO.email,
            phone = userDTO.phone,
            fatherName = userDTO.fam
        )
    }
}