package ru.manzharovn.data.models

import com.squareup.moshi.Json

data class HeroEntity(
    val name: String,
    val description: String?,
    @Json(name = "image")
    val imageSrc: Image
) {
    data class Image(
      @Json(name = "thumb_url") val iconUrl: String?
    )
}
