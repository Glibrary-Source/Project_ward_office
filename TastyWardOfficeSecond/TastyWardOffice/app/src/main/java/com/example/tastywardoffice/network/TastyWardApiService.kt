package com.example.tastywardoffice.network

import com.example.tastywardoffice.datamodel.*
import com.google.android.gms.maps.model.LatLng
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

//https://maps.googleapis.com/maps/api/geocode/json?latlng=37.5258883,126.8942541&key=AIzaSyBQvcrcZtRZb-fXeYqvVzmiGf3QDLiLoVY
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
    fun getStoreData(
        @Body data: JoinData
    ): Call<MyDTO>

    @POST("test1")
    fun getWholeData(
        @Body data: RequestType
    ): Call<WholeData>

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