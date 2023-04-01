package com.example.githubusers.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.api.ApiConfig
import com.example.githubusers.data.FavoriteUserEntity
import com.example.githubusers.data.FavoriteUserRepository
import com.example.githubusers.handler.Event
import com.example.githubusers.data.response.DetailUserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel() {

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    private val apiService = ApiConfig.getApiService()

    private val _userdetail = MutableLiveData<DetailUserResponse>()
    val userdetail = _userdetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _emptyResult = MutableLiveData<Event<String>>()
    val emptyResult: LiveData<Event<String>> = _emptyResult

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    // Initialize the LiveData object to hold the favorite user list
    private val favoriteUsersList = MutableLiveData<List<FavoriteUserEntity>>()

    // observe the allFavoriteUsers LiveData object
    init {
        favoriteUserRepository.allFavoriteUsers.observeForever {
            favoriteUsersList.value = it
        }
    }

    fun checkIsFavoriteUser(login: String): Boolean {
        return favoriteUsersList.value?.any { it.login == login } ?: false
    }


    fun getUserDetails(username: String) {
        _isLoading.value = true
        apiService.getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body()?.let { userResponse ->
                            _userdetail.value = userResponse
                            Log.d(TAG, "SUCCESS RESPONSE${_userdetail.value}")
                            // Check if the user is already in the favorites list
                            _isFavorite.value = checkIsFavoriteUser(userResponse.login!!)
                        }
                    } else {
                        Log.e(TAG, "EMPTY RESPONSE ${response.message()}")
                        _emptyResult.value =
                            Event("Sorry, Empty Response. API reached its limit?")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e(TAG, "FAILED RESPONSE ${t.message}")
                    _emptyResult.value =
                        Event("Sorry, Response Failed. Bad Connection?")
                }
            })
    }

    fun toggleFavorite() {
        _userdetail.value?.let { userDetail ->
            // Create a new FavoriteUserEntity using the user details
            val favoriteUser = FavoriteUserEntity(
                userDetail.login!!,
                userDetail.avatarUrl
            )
            viewModelScope.launch {
                if (isFavorite.value == true) {
                    favoriteUserRepository.deleteFavoriteUser(favoriteUser)
                    Log.d(TAG, "DELETE CALLED")
                } else {
                    favoriteUserRepository.insertFavoriteUser(favoriteUser)
                    Log.d(TAG, "INSERT CALLED")
                }
                //Update the isFavorite LiveData to reflect the current state
                //Maksudnya kan kalau habis di klik ketika belom favorit, sekarang jadi favorit
                //Jadi sekarang aku kembalikan kebalikannya, karena sekarang jadi favorit.
                _isFavorite.value = !isFavorite.value!!
            }
        }
    }
}
