package com.example.sinyal.di

import android.content.Context
import com.example.sinyal.api.APIConfig
import com.example.sinyal.db.EventDatabase
import com.example.sinyal.repository.MainRepository



object Injection {
    fun provideRepository(context: Context): MainRepository {
        val database = EventDatabase.getDatabase(context)
        val apiService = APIConfig.getApiService()
        return MainRepository(database, apiService)
    }
}