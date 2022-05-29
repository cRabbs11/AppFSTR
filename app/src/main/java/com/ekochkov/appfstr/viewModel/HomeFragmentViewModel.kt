package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.domain.Interactor
import com.ekochkov.appfstr.utils.PerevalAPIConstants
import com.ekochkov.appfstr.utils.PerevalRetrofit
import com.ekochkov.appfstr.utils.SingleLiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {

    var mountainsLiveData : LiveData<List<Mountain>>
    var selectedMountainsLiveData = MutableLiveData<List<Mountain>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    val userLiveData: LiveData<User?>


    @Inject
    lateinit var retrofit: PerevalRetrofit

    @Inject
    lateinit var interactor: Interactor

    private val compositeDisposable = CompositeDisposable()

    init {
        App.instance.dagger.inject(this)
        mountainsLiveData = interactor.getMountainsLiveData()
        userLiveData = interactor.getUserLiveData()
    }

    fun sendSelectedMountainsOnServer() {
        val completible = Completable.fromAction{
            interactor.sendMountainsOnServer(selectedMountainsLiveData.value!!, object : Interactor.ApiCallback {
                override fun onSuccess() {
                    toastEventLiveData.postValue(PerevalAPIConstants.ADD_MOUNTAINS_ON_SERVER_SUCCES)
                }

                override fun onFailure() {
                    toastEventLiveData.postValue(PerevalAPIConstants.ADD_MOUNTAINS_ON_SERVER_FAILURE)
                }
            })
            clearSelectedMountains()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
        compositeDisposable.add(completible)
    }

    fun addToSelectedMountains(mountain: Mountain) {
        val completible = Completable.fromAction{
            val oldList = selectedMountainsLiveData.value?: arrayListOf()
            val newList = arrayListOf<Mountain>()
            newList.addAll(oldList)
            newList.add(mountain)
            selectedMountainsLiveData.postValue(newList)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
        compositeDisposable.add(completible)
    }

    fun removeFromSelectedMountains(mountain: Mountain) {
        val completible = Completable.fromAction {
            val oldList = selectedMountainsLiveData.value as ArrayList<Mountain>
            oldList.remove(mountain)
            selectedMountainsLiveData.postValue(oldList)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
        compositeDisposable.add(completible)
    }

    fun clearSelectedMountains() {
        val completible = Completable.fromAction {
            selectedMountainsLiveData.postValue(arrayListOf<Mountain>())
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
        compositeDisposable.add(completible)
    }

    fun deleteUser() {
        interactor.deleteUser()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}