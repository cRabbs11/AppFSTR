package com.ekochkov.appfstr.data.entity

import androidx.room.*
import com.ekochkov.appfstr.data.AppDatabase
import java.io.Serializable

@Entity(tableName = AppDatabase.MOUNTAINS_TABLE_NAME)
data class Mountain(
        @PrimaryKey(autoGenerate = true) var cashedID: Int = 0,
        @ColumnInfo(name = "addTime") var addTime: String?,
        @ColumnInfo(name = "beautyTitle") var beautyTitle: String?,
        @ColumnInfo(name = "connect") var connect: String?,
        @Embedded(prefix = "coords_") var coords: Coords?,
        var images: List<Image>?,
        @Embedded(prefix = "level_") var level: Level?,
        @ColumnInfo(name = "otherTitles") var otherTitles: String?,
        @ColumnInfo(name = "title") var title: String?,
        @ColumnInfo(name = "type") var type: String?,
        @ColumnInfo(name = "id") var id: Int = ID_NOT_SET,
        @ColumnInfo(name = "userId") var userId: Int?,
): Serializable {
        constructor(): this(
                addTime = null,
                beautyTitle = null,
                connect = null,
                coords = null,
                level = null,
                title = null,
                type = null,
                userId = null,
                otherTitles = null,
                images = null)

        companion object {
                const val DIFFICULT_CATEGORY_NONE = "Н/К"
                const val DIFFICULT_CATEGORY_A1 = "А1"
                const val DIFFICULT_CATEGORY_A2 = "А2"
                const val DIFFICULT_CATEGORY_A3 = "А3"
                const val DIFFICULT_CATEGORY_B1 = "Б1"
                const val DIFFICULT_CATEGORY_B2 = "Б2"
                const val DIFFICULT_CATEGORY_B3 = "Б3"
                const val SUB_CATEGORY_NOT_SURE = "не уверен"
                const val SUB_CATEGORY_ESTIMATED = "оценочно"
                const val DIFFICULT_ADD = "*"

                const val TYPE_PASS = "pass"
                const val TYPE_SENDED = "sended"
                const val TYPE_ACCEPT = "accept"
                const val TYPE_PUBLISHED = "published"
                const val TYPE_DECLINE = "decline"

                const val ID_NOT_SET = -1
                const val ID_IS_WAITING_TO_SET = -2
        }
}