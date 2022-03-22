package ru.manzharovn.data

import retrofit2.HttpException
import ru.manzharovn.domain.models.ErrorEntity
import ru.manzharovn.domain.models.ErrorHandler
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.CancellationException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {

        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is CancellationException -> ErrorEntity.CoroutineCancel
            is HttpException -> {
                when(throwable.code()){

                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

                    else -> ErrorEntity.Unknown
                }
            } else -> ErrorEntity.Unknown
        }
    }
}