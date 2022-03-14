package ru.manzharovn.herofinder.presentation.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.manzharovn.data.network.ComicVineInfo
import ru.manzharovn.data.repository.PowersRepositoryImpl
import ru.manzharovn.domain.repository.PowersRepository

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ComicVineInfo.BASE_URL)
            .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

}