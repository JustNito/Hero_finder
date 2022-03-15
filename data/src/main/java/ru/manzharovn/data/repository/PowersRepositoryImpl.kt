package ru.manzharovn.data.repository

import ru.manzharovn.data.datasource.PowersRemoteDataSource
import ru.manzharovn.data.models.PowerEntity
import ru.manzharovn.data.network.ComicVineResponse
import ru.manzharovn.data.network.isMultipage
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.repository.PowersRepository
import javax.inject.Inject

class PowersRepositoryImpl @Inject constructor(val remoteDataSource: PowersRemoteDataSource) : PowersRepository {

    override suspend fun getPowers(): List<Power> {
        var response = remoteDataSource.getPowers()
        var listOfPowers = response.results
        if(response.isMultipage()) {
            listOfPowers = listOfPowers.toMutableList()
                listOfPowers.addAll(
                    remoteDataSource
                        .comicVineApi
                        .requestForRemainingPages(
                            response,
                            remoteDataSource::getPowers
                        )
                )
        }
        return mapPowersToDomain(listOfPowers)
    }

    private fun mapPowersToDomain(powers: List<PowerEntity>): List<Power> =
        powers.map { Power(it.name) }
}