package ru.manzharovn.domain.repository

import ru.manzharovn.domain.models.HeroFullDescription
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.models.Power

interface HeroesRepository {

    suspend fun getHeroFullDescription(id: Int): HeroFullDescription

    suspend fun getHeroShortDescription(): List<HeroShortDescription>

    suspend fun getHeroIdsByPowerId(id: Int): List<Int>

    suspend fun saveHero()
}