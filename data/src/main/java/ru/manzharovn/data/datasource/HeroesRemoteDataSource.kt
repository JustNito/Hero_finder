package ru.manzharovn.data.datasource

import ru.manzharovn.data.network.ComicVineApi
import ru.manzharovn.data.network.response.HeroByIdResponse
import ru.manzharovn.data.network.response.HeroesByPowerResponse
import javax.inject.Inject

class HeroesRemoteDataSource @Inject constructor(val comicVineApi: ComicVineApi) {
    suspend fun getHeroesIdByPowerId(id: Int): HeroesByPowerResponse =
        comicVineApi
            .retrofitService
            .getHeroesByPowerId(id)

    suspend fun getHeroById(id: Int): HeroByIdResponse =
        comicVineApi
            .retrofitService
            .getHeroById(id)
}