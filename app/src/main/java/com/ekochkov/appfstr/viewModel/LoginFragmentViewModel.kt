package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.User
import javax.inject.Inject

class LoginFragmentViewModel: ViewModel() {

    val userLiveData : LiveData<User?>

    @Inject
    lateinit var repository: Repository

    init {
        App.instance.dagger.inject(this)
        userLiveData = repository.getLiveDataUser()
    }

}