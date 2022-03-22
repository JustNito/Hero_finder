package ru.manzharovn.data.repository

import android.util.Log
import ru.manzharovn.data.datasource.HeroesRemoteDataSource
import ru.manzharovn.data.models.HeroEntity
import ru.manzharovn.data.utils.removeHtmlTags
import ru.manzharovn.domain.models.HeroFullDescription
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.repository.HeroesRepository
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
        val heroesRemoteDataSource: HeroesRemoteDataSource
    ) : HeroesRepository {
    override suspend fun getHeroFullDescription(id: Int): HeroFullDescription {
        TODO("Not yet implemented")
    }

    override suspend fun getHeroShortDescriptionById(id: Int): HeroShortDescription {
        val data = heroesRemoteDataSource.getHeroById(id).results
        Log.i("Repo", "hero id: $id")
        return data.mapHeroEntityToDomain()
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
            description = description?.removeHtmlTags(),
            imageSrc = imageSrc.iconUrl
        )

}