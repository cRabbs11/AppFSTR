package com.ekochkov.appfstr.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ekochkov.appfstr.data.dao.UserDao
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.utils.ImagesTypeConverter

@Database(entities = [User::class, Mountain::class], version = 1, exportSchema = false)
@TypeConverters(ImagesTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val MAIN_DB_NAME = "mountains_db"
        const val USERS_TABLE_NAME = "users_table"
        const val MOUNTAINS_TABLE_NAME = "mountains_table"
    }

    abstract fun userDao(): UserDao

}