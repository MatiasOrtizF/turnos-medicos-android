package com.mfo.turnosmedicos.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mfo.turnosmedicos.data.RepositoryImpl
import com.mfo.turnosmedicos.data.network.Constants.BASE_URL
import com.mfo.turnosmedicos.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTurnosMedicosApiService(retrofit: Retrofit): TurnosMedicosApiService {
        return retrofit.create(TurnosMedicosApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(turnosMedicosApiService: TurnosMedicosApiService): Repository {
        return RepositoryImpl(turnosMedicosApiService)
    }
}