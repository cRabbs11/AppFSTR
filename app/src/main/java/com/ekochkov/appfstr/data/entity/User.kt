package com.ekochkov.appfstr.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.appfstr.data.AppDatabase

@Entity(tableName = AppDatabase.USERS_TABLE_NAME,
        indices = [Index(
        value = ["name"],
        unique = true)])
data class User(
        @PrimaryKey(autoGenerate = true) var id: Int? = 0,
        @ColumnInfo(name = "name") var name: String?,
        @ColumnInfo(name = "secondName") var secondName: String?,
        @ColumnInfo(name = "fatherName") var fatherName: String?,
        @ColumnInfo(name = "email") var email: String?,
        @ColumnInfo(name = "phone") var phone: String?
)