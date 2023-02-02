package com.kapitalletter.tastywardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LocationDetailData (
    @Expose
    @SerializedName("results")
    val results: List<Results>
)

data class Results(
    @Expose
    @SerializedName("formatted_address")
    val formatted_address: String
)