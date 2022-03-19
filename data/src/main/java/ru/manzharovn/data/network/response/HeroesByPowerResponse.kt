package ru.manzharovn.data.network.response

import com.squareup.moshi.Json

data class HeroesByPowerResponse (
    @Json(name = "status_code") val statusCode: String,
    val error: String,
    @Json(name = "number_of_total_results") val numberOfTotalResults: String,
    @Json(name = "number_of_page_results") val numberOfPageResults: String,
    val limit: String,
    val offset: String,
    val results: Result

) {
    data class Result(
        @Json(name = "characters") val character: List<Character>
    )
    data class Character (
        val id: Int
    )
}




