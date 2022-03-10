package ru.manzharovn.domain.repository

import ru.manzharovn.domain.models.HeroFullDescription
import ru.manzharovn.domain.models.HeroShortDescription

interface HeroesRepository {

    suspend fun getHeroFullDescription(id: Int): HeroFullDescription

    suspend fun getHeroShortDescription(): List<HeroShortDescription>

    suspend fun saveHero()
}