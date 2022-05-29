package com.ekochkov.appfstr.data

import androidx.lifecycle.LiveData
import com.ekochkov.appfstr.data.dao.UserDao
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.data.entity.User

class Repository(private val userDao: UserDao) {

    fun getUserLiveData(): LiveData<User?> {
        return userDao.getLiveDataUser()
    }

    fun getUser(): User? {
        return userDao.getUser()
    }

    fun deleteUser(user: User): Int {
        return userDao.deleteUser(user)
    }

    fun createUser(user: User) : Long {
        return userDao.insertUser(user)
    }

    fun addMountain(mountain: Mountain): Long {
        return userDao.insertMountain(mountain)
    }

    fun getMountains(): List<Mountain> {
        return userDao.getMountains()
    }

    fun getMountain(cashedId: Int): Mountain? {
        return userDao.getMountain(cashedId)
    }

    fun updateMountain(mountain: Mountain): Int {
        return userDao.updateMountain(mountain)
    }

    fun getMountainsLiveData(): LiveData<List<Mountain>> {
        return userDao.getMountainsLiveData()
    }
}