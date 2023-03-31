package com.kapitalletter.wardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateReview (
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("store_Id")
    val store_Id: String,
    @Expose
    @SerializedName("reviewContext")
    val reviewContext: String,
    @Expose
    @SerializedName("reviewerNickname")
    val reviewerNickname: String,
    @Expose
    @SerializedName("reviewerUid")
    val reviewerUid: String,
)