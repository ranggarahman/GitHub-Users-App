package com.example.githubusers.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.api.ApiConfig
import com.example.githubusers.data.response.FollowResponse
import com.example.githubusers.data.response.FollowResponseItem
import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    companion object {
        private const val TAG = "FollowViewModel"
    }

    private val apiService = ApiConfig.getApiService()

    private val _usersfollowing = MutableLiveData<FollowResponse>()
    val userfollowing: LiveData<FollowResponse> = _usersfollowing

    private val _usersfollower = MutableLiveData<FollowResponse>()
    val userfollower: LiveData<FollowResponse> = _usersfollower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollower(username: String) {
        _isLoading.value = true

        apiService.getFollowers(username).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val json = response.body()
                    if ((json != null) && json.isJsonArray) {
                        // Response is an array
                        val followResponse = Gson().fromJson(json, Array<FollowResponseItem>::class.java)
                        _usersfollower.value = FollowResponse(ArrayList(followResponse.asList()))
                        Log.d(TAG, "SUCCESS RESPONSE, IS ARRAY : ${_usersfollower.value}")
                        Log.d(TAG, "RESPONSE BODY, IS ARRAY : ${response.body()}")
                    }
                } else {
                    Log.e(TAG, "EMPTY RESPONSE : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e(TAG, "FAILED RESPONSE: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true

        apiService.getFollowing(username).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val json = response.body()
                    if ((json != null) && json.isJsonArray) {
                        // Response is an array
                        val followResponse = Gson().fromJson(json, Array<FollowResponseItem>::class.java)
                        _usersfollowing.value = FollowResponse(ArrayList(followResponse.asList()))
                        Log.d(TAG, "SUCCESS RESPONSE, IS ARRAY : ${_usersfollowing.value}")
                        Log.d(TAG, "RESPONSE BODY, IS ARRAY : ${response.body()}")
                    }
                } else {
                    Log.e(TAG, "EMPTY RESPONSE : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "FAILED RESPONSE: ${t.message}")
            }
        })
    }
}
