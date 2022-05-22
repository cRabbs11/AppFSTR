package com.ekochkov.appfstr.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ekochkov.appfstr.data.dao.UserDao
import com.ekochkov.appfstr.data.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val USERS_TABLE_NAME = "users_table"
    }

    abstract fun userDao(): UserDao
}