package com.ekochkov.appfstr.data

import androidx.lifecycle.LiveData
import com.ekochkov.appfstr.data.dao.UserDao
import com.ekochkov.appfstr.data.entity.User
import io.reactivex.rxjava3.core.Observable

class Repository(private val userDao: UserDao) {

    fun getLiveDataUser(): LiveData<User?> {
        return userDao.getLiveDataUser()
    }

    fun getUser(): User? {
        return userDao.getUser()
    }

    fun createtUser(user: User) : Long {
        return userDao.insertUser(user)
    }
}