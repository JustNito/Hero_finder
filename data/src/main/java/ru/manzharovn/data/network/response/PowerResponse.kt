package ru.manzharovn.data.network.response

import com.squareup.moshi.Json
import ru.manzharovn.data.models.PowerEntity

data class PowerResponse (
    @Json(name = "status_code") val statusCode: String,
    val error: String,
    @Json(name = "number_of_total_results") val numberOfTotalResults: String,
    @Json(name = "number_of_page_results") val numberOfPageResults: String,
    val limit: String,
    val offset: String,
    val results: List<PowerEntity>
    )

fun PowerResponse.isMultipage(): Boolean = numberOfTotalResults > numberOfPageResults
