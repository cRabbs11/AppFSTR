package com.ekochkov.appfstr.domain

import androidx.lifecycle.LiveData
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.utils.PerevalRetrofit
import io.reactivex.rxjava3.core.Observable

class Interactor(val repository: Repository, retrofit: PerevalRetrofit) {

    fun getUser(): User? {
        return repository.getUser()
    }
}