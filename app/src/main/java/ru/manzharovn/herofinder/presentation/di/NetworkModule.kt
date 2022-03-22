package ru.manzharovn.herofinder.presentation.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.manzharovn.data.network.ComicVineInfo
import ru.manzharovn.data.repository.PowersRepositoryImpl
import ru.manzharovn.domain.repository.PowersRepository

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ComicVineInfo.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val url = original
                .url
                .newBuilder()
                .addQueryParameter("api_key",ComicVineInfo.API_KEY)
                .addQueryParameter("format", "json")
                .build()
            chain.proceed(
                original
                    .newBuilder()
                    .url(url)
                    .build()
            )
        }
        return okHttpClient.build()
    }
}