package com.ekochkov.appfstr.utils

import com.ekochkov.appfstr.data.entity.MountainDTO
import com.ekochkov.appfstr.data.entity.MountainsListDTO
import com.ekochkov.appfstr.data.entity.StatusDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.*

interface PerevalRetrofit {

    @GET("api/v1/pereval/")
    fun getMountains(): Call<MountainsListDTO>

    @POST("api/v1/pereval/")
    fun putMountain(
        @Body mountain: MountainDTO
    ): Observable<MountainDTO>

    @GET("api/v1/pereval/{id}/status")
    fun getMountainStatusById(
        @Path("id") id: Int,
    ): Observable<StatusDTO>

    @GET("api/v1/pereval/{id}")
    fun getMountainById(
        @Path("id") id: Int,
    ) : Observable<MountainDTO>

    @PUT("api/v1/pereval/{id}")
    fun updateMountainById(
        @Path("id") id: Int,
        @Body mountain: MountainDTO
    ) : Call<MountainDTO>
}