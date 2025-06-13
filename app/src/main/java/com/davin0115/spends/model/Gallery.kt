package com.davin0115.spends.model

import com.squareup.moshi.Json

data class Gallery(
    val id: String,
    @Json(name = "judul")
    val judul: String,
    @Json(name = "keterangan")
    val keterangan: String,
    val gambar: String,
    val mine: Int
)
