package ru.manzharovn.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.manzharovn.domain.models.ErrorHandler
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.repository.HeroesRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class SearchHeroesByPowerUseCase @Inject constructor(
    val heroesRepository: HeroesRepository,
    val errorHandler: ErrorHandler
) {

    suspend operator fun invoke(powers: List<Power>): Result<List<Int>> = try {
        withContext(coroutineContext) {

            val listOfHeroIds = powers.map { power ->
                async {
                    heroesRepository.getHeroIdsByPowerId(power.id)
                }
            }.awaitAll()
            var heroesIds = listOfHeroIds.first().toSet()
            listOfHeroIds.forEach { hero ->
                heroesIds = (hero intersect heroesIds)
            }
            Result.Success(
                data = heroesIds.toList()
            )
        }
    } catch (e: Throwable) {
        Result.Error(
            error = errorHandler.getError(e)
        )
    }
}

