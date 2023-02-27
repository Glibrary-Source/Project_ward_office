package com.kapitalletter.tastywardoffice.network

import com.kapitalletter.tastywardoffice.datamodel.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL =
    "https://asia-northeast3-project-ward-office.cloudfunctions.net/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TastyWardApiService {
    @POST("function-ward-office")
    fun getLocationDistanceTo(
        @Body data: RequestLocationData
    ): Call<FinalStoreDataModel>
}

object TastyWardApi{
    val service: TastyWardApiService by lazy {
        retrofit.create(TastyWardApiService::class.java)
    }
}