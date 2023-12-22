package com.choiri.bodybuddy.di

import android.content.Context
import com.choiri.bodybuddy.api.APIConfig
import com.choiri.bodybuddy.repository.MainRepository

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val apiService = APIConfig.getApiService()
        return MainRepository(apiService)
    }
}