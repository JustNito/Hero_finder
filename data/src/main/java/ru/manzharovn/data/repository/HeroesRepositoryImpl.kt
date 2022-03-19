package ru.manzharovn.data.repository

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.manzharovn.data.datasource.HeroesRemoteDataSource
import ru.manzharovn.data.models.HeroEntity
import ru.manzharovn.domain.models.HeroFullDescription
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.repository.HeroesRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class HeroesRepositoryImpl @Inject constructor(
        val heroesRemoteDataSource: HeroesRemoteDataSource
    ) : HeroesRepository {
    override suspend fun getHeroFullDescription(id: Int): HeroFullDescription {
        TODO("Not yet implemented")
    }

    override suspend fun getHeroShortDescription(): List<HeroShortDescription> {
        TODO("Not yet implemented")
    }

    override suspend fun getHeroIdsByPowerId(id: Int): List<Int> {
        val data = heroesRemoteDataSource
            .getHeroesIdByPowerId(id)
            .results
            .character.map {
                it.id
            }
        Log.i("Repo","power id: $id | data size: ${data.size}")
        return data
    }



    override suspend fun saveHero() {
        TODO("Not yet implemented")
    }

    private fun HeroEntity.mapHeroEntityToDomain(): HeroShortDescription =
        HeroShortDescription(
            name = name,
            description = description,
            imageSrc = imageSrc
        )

}