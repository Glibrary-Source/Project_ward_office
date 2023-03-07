package com.kapitalletter.wardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestLocationData (
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("MyGEOPoint")
    val MyGEOPoint: List<Double>
)

data class FinalStoreDataModel(
    @Expose
    @SerializedName("Filterstore")
    val Filterstore: List<Filterstore>
)

data class Filterstore(
    @Expose
    @SerializedName("document")
    val document: Documents
)

data class Documents(
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
    val storeGEOPoints: List<Double>,
    @Expose
    @SerializedName("storeId")
    val storeId: String,
    @Expose
    @SerializedName("storeMenuPictureUrls.menu")
    val storeMenuPictureUrlsMenu :List<String>,
    @Expose
    @SerializedName("storeMenuPictureUrls.store")
    val storeMenuPictureUrlsStore :List<String>,
    @Expose
    @SerializedName("storeMenus")
    val storeMenus: List<StoreMenus1>,
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

data class StoreMenus1(
    @Expose
    @SerializedName("menuPrice")
    val menuPrice: Int,
    @Expose
    @SerializedName("menuTitle")
    val menuTitle: String
)