package com.ekochkov.appfstr.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ekochkov.appfstr.data.AppDatabase

@Entity(tableName = AppDatabase.USERS_TABLE_NAME)
data class User(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "secondName") val secondName: String,
        @ColumnInfo(name = "fatherName") val fatherName: String,
        @ColumnInfo(name = "email") val email: String,
        @ColumnInfo(name = "phone") val phone: String
)