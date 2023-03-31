package com.kapitalletter.wardoffice.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReadReview (
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("docId")
    val store_Id: String
)

data class ReviewData(
    @Expose
    @SerializedName("docId")
    val docId: List<DocId>
)

data class DocId(
    @Expose
    @SerializedName("reviewContext")
    val reviewContext: String,
    @Expose
    @SerializedName("reviewDate")
    val reviewDate: String,
    @Expose
    @SerializedName("reviewerNickname")
    val reviewerNickname: String,
    @Expose
    @SerializedName("reviewerUid")
    val reviewerUid: String
)