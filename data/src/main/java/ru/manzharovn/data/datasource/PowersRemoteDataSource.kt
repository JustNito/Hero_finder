package ru.manzharovn.data.datasource

import ru.manzharovn.data.network.ComicVineApi
import ru.manzharovn.data.network.response.PowerResponse
import javax.inject.Inject

class PowersRemoteDataSource @Inject constructor(val comicVineApi: ComicVineApi) {
    suspend fun getPowers(offset: Int = 0): PowerResponse =
        comicVineApi
            .retrofitService
            .getPowers(offset)
}