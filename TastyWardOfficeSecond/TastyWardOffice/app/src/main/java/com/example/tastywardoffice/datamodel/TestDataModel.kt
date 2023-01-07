package com.example.tastywardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestDataModel(
    @Expose
    @SerializedName("facet_counts")
    val facet_counts: List<String>,
    @Expose
    @SerializedName("found")
    val found: Int,
    @Expose
    @SerializedName("hits")
    val hits: List<Hits>,
    @Expose
    @SerializedName("out_of")
    val out_of: Int,
    @Expose
    @SerializedName("page")
    val page: Int,
    @Expose
    @SerializedName("request_params")
    val request_params: Request_params,
    @Expose
    @SerializedName("search_cutoff")
    val search_cutoff: Boolean,
    @Expose
    @SerializedName("search_time_ms")
    val search_time_ms: Int
)

data class Request_params(
    @Expose
    @SerializedName("collection_name")
    val collection_name: String,
    @Expose
    @SerializedName("per_page")
    val per_page: Int,
    @Expose
    @SerializedName("q")
    val q: String
)

data class Hits(
    @Expose
    @SerializedName("document")
    val document: Document,
    @Expose
    @SerializedName("highlights")
    val highlights: List<String>,
    @Expose
    @SerializedName("text_match")
    val text_match: Int
)

data class Document(
    @Expose
    @SerializedName("district")
    val district: String,
    @Expose
    @SerializedName("docId")
    val docId: String,
    @Expose
    @SerializedName("id")
    val id: String,
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
    val storeMenus: List<StoreMenus>,
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

data class StoreMenus(
    @Expose
    @SerializedName("menuPrice")
    val menuPrice: Int,
    @Expose
    @SerializedName("menuTitle")
    val menuTitle: String
)