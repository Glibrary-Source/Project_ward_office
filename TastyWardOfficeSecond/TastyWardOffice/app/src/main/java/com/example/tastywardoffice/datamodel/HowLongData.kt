package com.example.tastywardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestLocationData (
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("storeGEOPoints")
    val storeGEOPoints: List<Double>
)

data class DistanceToData(
    @Expose
    @SerializedName("Howlong")
    val Howlong : List<LocationItems>
)

data class LocationItems(
    @Expose
    @SerializedName("district")
    val district: String,
    @Expose
    @SerializedName("docId")
    val docId: String,
    @Expose
    @SerializedName("storeCategory")
    val storeCategory: Int,
    @Expose
    @SerializedName("storeCntLikes")
    val storeCntLikes: Int,
    @Expose
    @SerializedName("storeGEOPoints")
    val storeGEOPoints: StoreGEOPoints,
    @Expose
    @SerializedName("storeId")
    val storeId: String,
    @Expose
    @SerializedName("storeMenuPictureUrls")
    val storeMenuPictureUrls :StoreMenuPictureUrls,
    @Expose
    @SerializedName("storePriceMax")
    val storePriceMax: Int,
    @Expose
    @SerializedName("storePriceMin")
    val storePriceMin: Int,
    @Expose
    @SerializedName("storeRating")
    val storeRating: Int,
    @Expose
    @SerializedName("storeTitle")
    val storeTitle: String
)

data class StoreMenuPictureUrls(
    @Expose
    @SerializedName("menu")
    val menu: List<String>,
    @Expose
    @SerializedName("store")
    val store: List<String>
)

data class StoreGEOPoints(
    @Expose
    @SerializedName("latitude")
    val latitude: Double,
    @Expose
    @SerializedName("longitude")
    val longitude: Double
)