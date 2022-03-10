package ru.manzharovn.domain.repository

import ru.manzharovn.domain.models.Power

interface PowersRepository {

    suspend fun getPowers(): List<Power>
}