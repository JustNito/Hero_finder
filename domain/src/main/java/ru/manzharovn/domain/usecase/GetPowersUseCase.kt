package ru.manzharovn.domain.usecase

import ru.manzharovn.domain.models.ErrorHandler
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.repository.PowersRepository
import java.lang.Exception
import javax.inject.Inject
import ru.manzharovn.domain.models.Result

class GetPowersUseCase @Inject constructor(
    val powersRepository: PowersRepository,
    val errorHandler: ErrorHandler
    ) {
    suspend operator fun invoke(): Result<List<Power>> = try {
        Result.Success(
            data = powersRepository.getPowers()
        )
    } catch (e: Throwable) {
        Result.Error(
            error = errorHandler.getError(e)
        )
    }
}