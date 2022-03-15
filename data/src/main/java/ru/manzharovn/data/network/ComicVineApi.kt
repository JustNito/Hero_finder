package ru.manzharovn.data.network

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import ru.manzharovn.data.models.PowerEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicVineApi @Inject constructor(val retrofit: Retrofit) {
    interface ComicVineApiService {
        @GET("powers?field_list=name")
        suspend fun getPowers(@Query("offset") offset: Int): ComicVineResponse<PowerEntity>
    }
    val retrofitService: ComicVineApiService by lazy {
        retrofit.create(ComicVineApiService::class.java)
    }

    suspend fun <T> requestForRemainingPages(
        response: ComicVineResponse<T>,
        request: suspend (offset: Int) -> ComicVineResponse<T>
    ): List<T> {
        var results = response.results.toMutableList()
        val numberOfTotalResults = response.numberOfTotalResults.toInt()
        val limit = response.limit.toInt()
        var numberOfPages: Int = (numberOfTotalResults / limit) - if(numberOfTotalResults % limit == 0) 1 else 0
        for(page in 1..numberOfPages) {
            var offset = page * limit
            results.addAll(request(offset).results)
        }
        return results.toList()
    }
}