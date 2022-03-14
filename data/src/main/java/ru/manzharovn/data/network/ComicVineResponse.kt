package ru.manzharovn.data.network

import com.squareup.moshi.Json

data class ComicVineResponse<T> (
    @Json(name = "status_code") val statusCode: String,
    val error: String,
    @Json(name = "number_of_total_results") val numberOfTotalResults: String,
    @Json(name = "number_of_page_results") val numberOfPageResults: String,
    val limit: String,
    val offset: String,
    val results: List<T>
    )