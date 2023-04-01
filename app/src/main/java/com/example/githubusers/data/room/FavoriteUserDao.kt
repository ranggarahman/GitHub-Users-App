package com.example.githubusers.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusers.data.FavoriteUserEntity

@Dao
interface FavoriteUserDao {

    @Query("SELECT * FROM favoriteusers")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteUser(favoriteUser: FavoriteUserEntity)

    @Delete
    fun deleteFavoriteUser(favoriteUser: FavoriteUserEntity)

    @Query("SELECT COUNT(*) FROM favoriteusers")
    fun getNumberOfRows(): Int
}