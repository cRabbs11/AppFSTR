package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.domain.Interactor
import javax.inject.Inject

class LoginFragmentViewModel: ViewModel() {

    val userLiveData : LiveData<User?>

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        userLiveData = interactor.getUserLiveData()
    }

}