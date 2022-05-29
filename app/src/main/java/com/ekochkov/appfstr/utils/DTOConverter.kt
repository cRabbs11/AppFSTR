package com.ekochkov.appfstr.utils

import com.ekochkov.appfstr.data.entity.*


object DTOConverter {

    fun fromMountainDTOToMountain(mountainDTO: MountainDTO): Mountain {
        val images = arrayListOf<Image>()
        mountainDTO.images.forEach {
            images.add(fromImageDTOToImage(it))
        }

        return Mountain(
            addTime = mountainDTO.add_time,
            beautyTitle = mountainDTO.beautyTitle,
            connect = mountainDTO.connect,
            coords = fromCooredsDTOToCoords(mountainDTO.coords),
            id = mountainDTO.id,
            images = images,
            level = fromLevelDTOToLevel(mountainDTO.level),
            otherTitles = mountainDTO.other_titles,
            title = mountainDTO.title,
            type = mountainDTO.type,
            //userId = fromUserDTOToUser(mountainDTO.user)
            userId = mountainDTO.user.id
        )
    }

    private fun fromCooredsDTOToCoords(coordsDTO: CoordsDTO): Coords {
        return Coords(
            height = coordsDTO.height,
            latitude = coordsDTO.latitude,
            longitude = coordsDTO.longitude
        )
    }

    private fun fromCooredsToCoordsDTO(coords: Coords): CoordsDTO {
        return CoordsDTO(
            height = coords.height,
            latitude = coords.latitude,
            longitude = coords.longitude
        )
    }

    private fun fromImageDTOToImage(imageDTO: ImageDTO): Image {
        return Image(
            title = imageDTO.title,
            url = imageDTO.url
        )
    }

    private fun fromImageToImageDTO(image: Image): ImageDTO {
        return ImageDTO(
            title = image.title,
            url = image.url
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

    private fun fromLevelToLevelDTO(level: Level): LevelDTO {
        return LevelDTO(
            autumn = level.autumn,
            spring = level.spring,
            summer = level.summer,
            winter = level.winter
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

    private fun fromUsertToUserDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id!!,
            name = user.name!!,
            fam = user.secondName!!,
            email = user.email!!,
            phone = user.phone!!
        )
    }

    fun fromMountainToMountainDTO(mountain: Mountain, user: User): MountainDTO {
        val userDTO = fromUsertToUserDTO(user)
        var list = arrayListOf<ImageDTO>()

        return MountainDTO(
            add_time = mountain.addTime!!,
            beautyTitle = mountain.beautyTitle!!,
            connect = mountain.connect!!,
            coords = fromCooredsToCoordsDTO(mountain.coords!!),
            id = mountain.id,
            images = list,
            level = fromLevelToLevelDTO(mountain.level!!),
            other_titles = mountain.otherTitles!!,
            title = mountain.title!!,
            type = mountain.type!!,
            user = userDTO
        )
    }
}