package com.example.githubusers.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.api.ApiConfig
import com.example.githubusers.handler.Event
import com.example.githubusers.data.response.ItemsItem
import com.example.githubusers.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){

    companion object {
        private const val TAG = "UserViewModel"
    }

    private val apiService = ApiConfig.getApiService()

    private val _users = MutableLiveData<List<ItemsItem>>()
    val users: LiveData<List<ItemsItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _emptyResult = MutableLiveData<Event<String>>()
    val emptyResult: LiveData<Event<String>> = _emptyResult

    fun searchUsers(query: String) {
        _isLoading.value = true
        if (query.isBlank()) {
            _users.value = emptyList()
            return
        }

        apiService.getUsers(query, 1, 100)
            .enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { userResponse ->
                        _users.value = (userResponse.items!!) as List<ItemsItem>?
                        Log.d(TAG, "RESPONSE SUCCESS ${_users.value}")

                        if(_users.value?.isEmpty() == true){
                            _emptyResult.value = Event("No Users Found!")
                        }
                    }
                } else {
                    Log.e(TAG, "EMPTY RESPONSE : ${response.message()}")
                    _emptyResult.value = Event("Sorry, Response is Empty. API reached limit?")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "FAILURE : ${t.message}")
                _emptyResult.value = Event("Sorry, Response Failed. Bad Connection?")
            }
        })
    }
}
