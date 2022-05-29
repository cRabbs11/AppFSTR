package com.ekochkov.appfstr.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekochkov.appfstr.data.AppDatabase
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.utils.ImagesTypeConverter
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserDao {

    @Query("SELECT * FROM ${AppDatabase.USERS_TABLE_NAME}")
    fun getLiveDataUser() : LiveData<User?>

    @Query("SELECT * FROM ${AppDatabase.USERS_TABLE_NAME}")
    fun getUser() : User?

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMountain(mountain: Mountain): Long

    @Update
    fun updateMountain(mountain: Mountain): Int

    @Query("SELECT * FROM ${AppDatabase.MOUNTAINS_TABLE_NAME}")
    fun getMountains() : List<Mountain>

    @Query("SELECT * FROM ${AppDatabase.MOUNTAINS_TABLE_NAME} WHERE cashedID LIKE:cashedId")
    fun getMountain(cashedId: Int) : Mountain?

    @Query("SELECT * FROM ${AppDatabase.MOUNTAINS_TABLE_NAME}")
    fun getMountainsLiveData() : LiveData<List<Mountain>>
}