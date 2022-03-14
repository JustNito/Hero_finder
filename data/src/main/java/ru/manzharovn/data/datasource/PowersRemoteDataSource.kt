package ru.manzharovn.data.datasource

import ru.manzharovn.data.models.PowerEntity
import ru.manzharovn.data.network.ComicVineApi
import ru.manzharovn.data.network.ComicVineResponse
import javax.inject.Inject

class PowersRemoteDataSource @Inject constructor(val comicVineApi: ComicVineApi) {
    suspend fun getPowers(): ComicVineResponse<PowerEntity> =
        comicVineApi
            .retrofitService
            .getPowers()
}