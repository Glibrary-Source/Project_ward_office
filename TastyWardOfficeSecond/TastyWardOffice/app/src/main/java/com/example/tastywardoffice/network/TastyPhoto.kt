package com.example.tastywardoffice.network

import com.squareup.moshi.Json

data class TastyPhoto (
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String
    )