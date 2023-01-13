package com.example.tastywardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LocationDetailData (
//    @Expose
//    @SerializedName("plus_code")
//    val plus_code: Plus_code,
    @Expose
    @SerializedName("results")
    val results: List<Results>
//    @Expose
//    @SerializedName("status")
//    val status: String
)

data class Results(
//    @Expose
//    @SerializedName("address_components")
//    val address_components: List<Address_components>,
    @Expose
    @SerializedName("formatted_address")
    val formatted_address: String
//    @Expose
//    @SerializedName("geometry")
//    val geometry: Geometry,
//    @Expose
//    @SerializedName("place_id")
//    val place_id: String,
//    @Expose
//    @SerializedName("plus_code")
//    val plus_code: Plus_code,
//    @Expose
//    @SerializedName("types")
//    val types: List<String>
)

//data class Geometry(
//    @Expose
//    @SerializedName("location")
//    val location: Location,
//    @Expose
//    @SerializedName("location_type")
//    val location_type: String,
//    @Expose
//    @SerializedName("viewport")
//    val viewport: Viewport
//)
//
//data class Viewport(
//    @Expose
//    @SerializedName("northeast")
//    val northeast: Northeast,
//    @Expose
//    @SerializedName("southwest")
//    val southwest: Southwest
//)
//
//data class Southwest(
//    @Expose
//    @SerializedName("lat")
//    val lat: Double,
//    @Expose
//    @SerializedName("lng")
//    val lng: Double
//)
//
//data class Northeast(
//    @Expose
//    @SerializedName("lat")
//    val lat: Double,
//    @Expose
//    @SerializedName("lng")
//    val lng: Double
//)
//
//data class Location(
//    @Expose
//    @SerializedName("lat")
//    val lat: Double,
//    @Expose
//    @SerializedName("lng")
//    val lng: Double
//)
//
//data class Address_components(
//    @Expose
//    @SerializedName("long_name")
//    val long_name: String,
//    @Expose
//    @SerializedName("short_name")
//    val short_name: String,
//    @Expose
//    @SerializedName("types")
//    val types: List<String>
//)
//
//data class Plus_code(
//    @Expose
//    @SerializedName("compound_code")
//    val compound_code: String,
//    @Expose
//    @SerializedName("global_code")
//    val global_code: String
//)
