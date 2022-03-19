package ru.manzharovn.data.models

import com.squareup.moshi.Json

data class HeroEntity(
    val name: String,
    val description: String,
    @Json(name = "image")
    val imageSrc: String
)
