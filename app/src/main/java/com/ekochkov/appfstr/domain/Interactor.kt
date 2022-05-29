package com.ekochkov.appfstr.domain

import androidx.lifecycle.LiveData
import com.ekochkov.appfstr.data.Repository
import com.ekochkov.appfstr.data.entity.*
import com.ekochkov.appfstr.utils.DTOConverter
import com.ekochkov.appfstr.utils.PerevalRetrofit
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executors

class Interactor(private val repository: Repository, private val retrofit: PerevalRetrofit) {

    init {
        updateMountainsByServer()
    }

    fun getUser(): User? {
        return repository.getUser()
    }

    fun getUserLiveData(): LiveData<User?> {
        return repository.getUserLiveData()
    }

    fun createUser(user: User): Long {
        return repository.createUser(user)
    }

    fun getMountains(): List<Mountain> {
        return repository.getMountains()
    }

    fun getMountainFromBD(cashedId: Int) : Mountain? {
        return repository.getMountain(cashedId)
    }

    fun getMountainsLiveData(): LiveData<List<Mountain>> {
        return repository.getMountainsLiveData()
    }

    fun addMountainInBD(mountain: Mountain): Long {
        return repository.addMountain(mountain)
    }

    fun sendMountainsOnServer(list: List<Mountain>, callback: ApiCallback) {
        //подсчет ответов от Api
        val callbackResults = arrayListOf<Boolean>()

        val user = repository.getUser()

        val partCallback = object : ApiCallback {
            override fun onSuccess() {
                callbackResults.add(true)
                if (callbackResults.size == list.size) {
                    callback.onSuccess()
                }
            }

            override fun onFailure() {
                callbackResults.add(false)
                if (callbackResults.size == list.size) {
                    callback.onFailure()
                }
            }
        }

        list.forEach {
            sendMountainOnServer(it, user!!, partCallback)
        }
    }

    private fun sendMountainOnServer(mountain: Mountain, user: User, callback: ApiCallback?) {
        val mountainDTO = DTOConverter.fromMountainToMountainDTO(mountain, user)
        retrofit.putMountain(mountainDTO).subscribeOn(Schedulers.io())
            .map {
                val recieveMountain = DTOConverter.fromMountainDTOToMountain(it)
                recieveMountain.cashedID = mountain.cashedID
                recieveMountain
            }.subscribeBy(
                onError = {
                    println("!!! it: ${it.message}")
                    callback?.onFailure() },
                onNext = {
                    repository.updateMountain(it)
                    callback?.onSuccess()})
    }

    //работает
    fun getMountainFromServerById(mountain: Mountain) {
        retrofit.getMountainById(mountain.id).subscribeOn(Schedulers.io())
            .map {
                val mountain = DTOConverter.fromMountainDTOToMountain(it)
                mountain.cashedID = mountain.cashedID
                mountain
            }
            .subscribeBy(
                onError = {
                    println("!!! ${it.printStackTrace().toString()}")
                },
                onNext = {
                    repository.updateMountain(it)
                }
            )
    }

    fun updateMountainOnServerById(mountain: Mountain) {
        val user = repository.getUser()

        val mountainDTO = DTOConverter.fromMountainToMountainDTO(mountain, user!!)
        retrofit.updateMountainById(mountainDTO.id, mountainDTO).enqueue(object: retrofit2.Callback<MountainDTO> {
            override fun onResponse(call: Call<MountainDTO>, response: Response<MountainDTO>) {
                println("!!! onUpdateMountainOnServerById onResponse")
                println("!!! response ${response.body()}")
            }

            override fun onFailure(call: Call<MountainDTO>, t: Throwable) {
                println("!!! onUpdateMountainOnServerById onFailure")
                println("!!! ${t.printStackTrace().toString()}")
                println("!!! message: ${t.message}")
            }

        })
        println("!!! put mountain on server done")
    }

    private fun getMountainStatusFromServer(mountain: Mountain) {
        retrofit.getMountainStatusById(54).subscribeOn(Schedulers.io())
            .map {
                val status = it.status
                mountain.type = status
                mountain
            }
            .subscribeBy(
                onError = {
                    println("!!! ${it.printStackTrace().toString()}")
                },
                onNext = {
                    repository.updateMountain(it)
                }
            )
    }

    fun updateMountainInDB(mountain: Mountain): Int {
        return repository.updateMountain(mountain)
    }

    private fun updateMountainsByServer() {
        Executors.newSingleThreadExecutor().execute {
            val user = repository.getUser()
            var list = repository.getMountains()
            list.forEach {
                when (it.id) {
                    Mountain.ID_IS_WAITING_TO_SET -> {
                        sendMountainOnServer(it, user!!, null)
                    }
                    Mountain.ID_NOT_SET -> {
                        getMountainStatusFromServer(it)
                    }
                    else -> {
                        if (it.type == Mountain.TYPE_PUBLISHED || it.type == Mountain.TYPE_DECLINE)
                        getMountainStatusFromServer(it)
                    }
                }
            }
        }
    }

    fun deleteUser() {
        Completable.fromAction {
            val user = repository.getUser()
            if (user!=null) {
                repository.deleteUser(user)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()

    }

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }
}

