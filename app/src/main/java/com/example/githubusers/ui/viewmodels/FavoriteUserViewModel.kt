package com.example.githubusers.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.FavoriteUserEntity
import com.example.githubusers.data.FavoriteUserRepository

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository): ViewModel() {
    val favoriteUsers: LiveData<List<FavoriteUserEntity>> = favoriteUserRepository.allFavoriteUsers
}