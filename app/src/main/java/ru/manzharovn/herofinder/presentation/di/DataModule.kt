package ru.manzharovn.herofinder.presentation.di

import dagger.Binds
import dagger.Module
import ru.manzharovn.data.ErrorHandlerImpl
import ru.manzharovn.data.repository.PowersRepositoryImpl
import ru.manzharovn.domain.models.ErrorHandler
import ru.manzharovn.domain.repository.PowersRepository

@Module
abstract class DataModule {

    @Binds
    abstract fun provideRepository(powerRepository: PowersRepositoryImpl): PowersRepository

    @Binds
    abstract fun provideErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler
}