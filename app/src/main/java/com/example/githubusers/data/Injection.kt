package com.example.githubusers.data

import android.content.Context
import com.example.githubusers.data.api.ApiConfig
import com.example.githubusers.data.room.FavoriteUserDatabase

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        val appExecutors = AppExecutors()
        return FavoriteUserRepository.getInstance(apiService, dao, appExecutors)
    }
}