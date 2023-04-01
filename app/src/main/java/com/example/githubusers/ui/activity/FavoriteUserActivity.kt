package com.example.githubusers.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.ui.viewmodels.ViewModelFactory
import com.example.githubusers.databinding.ActivityFavoriteUserBinding
import com.example.githubusers.handler.RecyclerViewHelper
import com.example.githubusers.data.response.FavoriteUserListItem
import com.example.githubusers.ui.viewmodels.FavoriteUserViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private val favoriteUsersViewModel by viewModels<FavoriteUserViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUsersViewModel.favoriteUsers.observe(this) { favoriteUsers ->
            val userListItemFavorites = favoriteUsers.map { favoriteUser ->
                FavoriteUserListItem(
                    favoriteUser.login,
                    favoriteUser.avatarUrl
                )
            }
            RecyclerViewHelper.setupRecyclerView(
                this@FavoriteUserActivity,
                binding.recyclerViewUserFavorite,
                ArrayList(userListItemFavorites)
            )
        }
    }
}