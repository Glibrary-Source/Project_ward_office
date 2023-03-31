package com.kapitalletter.wardoffice.network

import com.kapitalletter.wardoffice.datamodel.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

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

    @POST("function-ward-office")
    fun createReview(
        @Body data: CreateReview
    ): Call<CreateReview>

    @POST("function-ward-office")
    fun readReview(
        @Body data: ReadReview
    ): Call<ReviewData>

}

object TastyWardApi{
    val service: TastyWardApiService by lazy {
        retrofit.create(TastyWardApiService::class.java)
    }
}