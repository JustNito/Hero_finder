package ru.manzharovn.data.network

import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.manzharovn.data.models.PowerEntity
import ru.manzharovn.data.network.response.PowerResponse
import ru.manzharovn.data.network.response.HeroByIdResponse
import ru.manzharovn.data.network.response.HeroesByPowerResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
class ComicVineApi @Inject constructor(val retrofit: Retrofit) {
    interface ComicVineApiService {
        @GET("powers?field_list=name,id")
        suspend fun getPowers(@Query("offset") offset: Int): PowerResponse

        @GET("power/4035-{id}?fields_list=characters")
        suspend fun getHeroesByPowerId(@Path("id") id: Int): HeroesByPowerResponse

        @GET("character/4005-{id}?fields_list=name,description,image")
        suspend fun getHeroById(@Path("id") id: Int): HeroByIdResponse
    }
    val retrofitService: ComicVineApiService by lazy {
        retrofit.create(ComicVineApiService::class.java)
    }

    suspend fun requestForRemainingPages(
        response: PowerResponse,
        request: suspend (offset: Int) -> PowerResponse
    ): List<PowerEntity> = withContext(coroutineContext) {
        val results = mutableListOf<Deferred<List<PowerEntity>>>()
        val numberOfTotalResults = response.numberOfTotalResults.toInt()
        val limit = response.limit.toInt()
        val numberOfPages: Int = (numberOfTotalResults / limit) - if(numberOfTotalResults % limit == 0) 1 else 0
        for(page in 1..numberOfPages) {
            val offset = page * limit
            results.add(async { request(offset).results })
        }
        return@withContext results.awaitAll().flatten()
    }
}