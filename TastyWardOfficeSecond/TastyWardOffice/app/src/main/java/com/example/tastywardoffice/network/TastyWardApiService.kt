package com.example.tastywardoffice.network

import com.example.tastywardoffice.datamodel.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL =
    "https://asia-northeast3-delicious-project-e3bed.cloudfunctions.net/"

private const val GOOGLEGEO_URL =
    "https://maps.googleapis.com/maps/api/geocode/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

private val retrofit2 = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(GOOGLEGEO_URL)
    .build()

interface TastyWardApiService {

    @POST("test1")
    fun getLocationDistanceTo(
        @Body data: RequestLocationData
    ): Call<FinalStoreDataModel>

    @GET("json")
    fun getDetailLocation(
        @Query("latlng") locationLatLng: String,
        @Query("key") key: String,
        @Query("language") language: String
    ): Call<LocationDetailData>

}

object TastyWardApi{
    val service: TastyWardApiService by lazy {
        retrofit.create(TastyWardApiService::class.java)
    }
}

object TastyWardApi2{
    val service: TastyWardApiService by lazy {
        retrofit2.create(TastyWardApiService::class.java)
    }
}