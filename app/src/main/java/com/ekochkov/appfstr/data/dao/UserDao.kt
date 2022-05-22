package com.ekochkov.appfstr.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekochkov.appfstr.data.AppDatabase
import com.ekochkov.appfstr.data.entity.User
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserDao {

    @Query("SELECT * FROM ${AppDatabase.USERS_TABLE_NAME}")
    fun getLiveDataUser() : LiveData<User?>

    @Query("SELECT * FROM ${AppDatabase.USERS_TABLE_NAME}")
    fun getUser() : User?

    @Update
    fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long
}