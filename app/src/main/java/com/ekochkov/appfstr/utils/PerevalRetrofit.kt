package com.ekochkov.appfstr.utils

import com.ekochkov.appfstr.data.entity.MountainDTO
import com.ekochkov.appfstr.data.entity.MountainsListDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PerevalRetrofit {

    @GET("api/v1/pereval/")
    fun getMountains(): Call<MountainsListDTO>

    @POST("api/v1/pereval/")
    fun putMountain(@Body mountainDTO: MountainDTO): Call<String>
}