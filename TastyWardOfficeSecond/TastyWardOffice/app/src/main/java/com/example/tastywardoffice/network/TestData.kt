package com.example.tastywardoffice.network

import com.google.gson.annotations.SerializedName

data class JoinData(
    @SerializedName("type")
    val type: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("storeCategory")
    val storeCategory: Int,
    @SerializedName("storePriceMin")
    val storePriceMin: Int,
    @SerializedName("storePriceMax")
    val storePriceMax: Int
)

data class MyDTO(
    @SerializedName("1Nr9s2Jibfz7v2qFvJRb")
    val firstData: First,
    @SerializedName("Jot1GCUffg32tYyYpIlN")
    val secondData: First
)

data class First(
    @SerializedName("district")
    val district: String,
    @SerializedName("storeId")
    val storeId: String,
    @SerializedName("storeGEOPoints")
    val storeGEOPoints: Geo,
    @SerializedName("storeMenuPictureUrls ")
    val storeMenuPictureUrls : List<String>
)

data class Geo(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
