package ru.manzharovn.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.manzharovn.domain.models.ErrorHandler
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.repository.HeroesRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetHeroShortDescriptionByIdsUseCase @Inject constructor(
   val heroesRepository: HeroesRepository,
   val errorHandler: ErrorHandler
) {
    suspend operator fun invoke(ids: List<Int>): Result<List<HeroShortDescription>> = try {
        withContext(coroutineContext) {
            Result.Success(
                data = ids.map { id ->
                    async {
                        heroesRepository.getHeroShortDescriptionById(id)
                    }
                }.awaitAll()
            )
        }
    } catch (e: Throwable) {
        Result.Error(
            error = errorHandler.getError(e)
        )
    }
}
