package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.utils.SingleLiveEvent
import java.util.concurrent.Executors
import javax.inject.Inject

class RegisterFragmentViewModel: ViewModel() {

    val userId = SingleLiveEvent<Long>()

    @Inject
    lateinit var repository: Repository

    init {
        App.instance.dagger.inject(this)
    }

    fun registerUser(user: User) {
        Executors.newSingleThreadExecutor().execute {
            userId.postValue(repository.createtUser(user))
        }
    }
}