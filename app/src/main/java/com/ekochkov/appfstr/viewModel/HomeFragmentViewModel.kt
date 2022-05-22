package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.data.entity.MountainsListDTO
import com.ekochkov.appfstr.utils.Converter
import com.ekochkov.appfstr.utils.PerevalRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {

    val some = "Some"

    @Inject
    lateinit var retrofit: PerevalRetrofit

    init {
        App.instance.dagger.inject(this)
        getMountains()
    }

    fun getMountains() {
        retrofit.getMountains().enqueue(object: Callback<MountainsListDTO> {
            override fun onResponse(
                call: Call<MountainsListDTO>,
                response: Response<MountainsListDTO>
            ) {
                println("!!! onResponse")
                println("!!! ${response.body().toString()}")

                var mountains = arrayListOf<Mountain>()
                response.body()?.forEach {
                    mountains.add(Converter.fromMountainDTOToMountain(it))
                }
            }

            override fun onFailure(call: Call<MountainsListDTO>, t: Throwable) {
                println("!!! onFailure")
                println("!!! ${t.printStackTrace().toString()}")
                println("!!! ${t.message}")
            }

        })
    }

}