package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.domain.Interactor
import com.ekochkov.appfstr.utils.SingleLiveEvent
import java.util.concurrent.Executors
import javax.inject.Inject

class RegisterFragmentViewModel: ViewModel() {

    val userId = SingleLiveEvent<Long>()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun registerUser(user: User) {
        Executors.newSingleThreadExecutor().execute {
            val id = userId.postValue(interactor.createUser(user))
            println("!!! createuserId = $id")
        }
    }

    fun createTestUser() {
        println("!!! registerUser...")
        val user = User(
            email = "max@pain.com",
            secondName = "пэйн",
            fatherName = "булеттаймович",
            id = 154,
            name = "макс",
            phone = "+7 966 543 12 34")

        registerUser(user)
    }
}