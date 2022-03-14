package ru.manzharovn.data.network

import retrofit2.Retrofit
import retrofit2.http.GET
import ru.manzharovn.data.models.PowerEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicVineApi @Inject constructor(val retrofit: Retrofit) {
    interface ComicVineApiService {
        //@Headers("api-key: " + ComicVineInfo.API_KEY)
        @GET("powers?api_key=${ComicVineInfo.API_KEY}&format=json&field_list=name")
        suspend fun getPowers(): ComicVineResponse<PowerEntity>
    }
    val retrofitService: ComicVineApiService by lazy {
        retrofit.create(ComicVineApiService::class.java)
    }
}