package com.example.plantcare.util

import android.content.Context
import com.example.plantcare.data.network.PlantService
import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.data.network.UserPlantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
    @Singleton
    @Provides
    fun provideUserApiService(sessionManager: SessionManager): UserApiService {
        val retrofitService = RetrofitService(sessionManager)
        return retrofitService.userApi
    }

    @Singleton
    @Provides
    fun provideUserPlantService(sessionManager: SessionManager): UserPlantService {
        val retrofitService = RetrofitService(sessionManager)
        return retrofitService.userPlantApi
    }

    @Singleton
    @Provides
    fun providePlantService(sessionManager: SessionManager): PlantService {
        val retrofitService = RetrofitService(sessionManager)
        return retrofitService.plantApi
    }

}