package com.example.githubusers.data.api

import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.data.response.UserResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<UserResponse>

    @GET("users/{login}")
    fun getDetailUser(
        @Path("login") username: String
    ): Call<DetailUserResponse>

    @GET("users/{login}/followers")
    fun getFollowers(
        @Path("login") username: String
    ): Call<JsonElement>


    @GET("users/{login}/following")
    fun getFollowing(
        @Path("login") username: String
    ): Call<JsonElement>
}
