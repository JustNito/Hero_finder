package ru.manzharovn.data.repository

import ru.manzharovn.data.datasource.PowersRemoteDataSource
import ru.manzharovn.data.models.PowerEntity
import ru.manzharovn.data.network.ComicVineResponse
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.repository.PowersRepository
import javax.inject.Inject

class PowersRepositoryImpl @Inject constructor(val remoteDataSource: PowersRemoteDataSource) : PowersRepository {

    override suspend fun getPowers(): List<Power> =
        mapPowersToDomain(remoteDataSource.getPowers().results)

    private fun mapPowersToDomain(powers: List<PowerEntity>): List<Power> =
        powers.map { Power(it.name) }
}