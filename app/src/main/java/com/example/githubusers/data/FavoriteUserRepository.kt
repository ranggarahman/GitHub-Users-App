package com.example.githubusers.data

import android.util.Log
import com.example.githubusers.data.api.ApiService
import com.example.githubusers.data.room.FavoriteUserDao

class FavoriteUserRepository(
    private val apiService: ApiService,
    private var userfavDao: FavoriteUserDao,
    private val appExecutors: AppExecutors,
) {

    val allFavoriteUsers = userfavDao.getAllFavoriteUsers()
    private var row: Int? = 0

    fun insertFavoriteUser(favoriteUser: FavoriteUserEntity) {
        try {
            appExecutors.diskIO.execute{
                userfavDao.insertFavoriteUser(favoriteUser)
                row = userfavDao.getNumberOfRows()
            }
            Log.d(TAG, "Insert event called : Row = $row")
        }catch (e: Exception){
            Log.e(TAG, "FAIL : ${e.message}")
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUserEntity) {
        try {
            appExecutors.diskIO.execute{
                userfavDao.deleteFavoriteUser(favoriteUser)
            }
            Log.d(TAG, "Delete event called: Row = $row")
        }catch (e: Exception){
            Log.e(TAG, "FAIL : ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: FavoriteUserDao,
            appExecutors: AppExecutors
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }

        private const val TAG =  "REPOSITORY"
    }
}
