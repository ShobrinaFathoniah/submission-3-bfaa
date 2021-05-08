package com.dicoding.picodiploma.githubusers.data.network.client

import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.data.model.UserDetailModel
import com.dicoding.picodiploma.githubusers.data.model.response.SearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

object ApiClient {
    private const val BASE_URL = "https://api.github.com/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val instance = retrofit.create(Api::class.java)!!
}

interface Api {
    @GET("search/users")
    fun getSearch(
            @Query("q") query: String,
            @Header("Authorization") token: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String,
                      @Header("Authorization") token: String): Call<UserDetailModel>

    @GET("users/{username}/followers")
    fun getFollowersUser(@Path("username") username: String,
                         @Header("Authorization") token: String): Call<ArrayList<MiniUserModel>>

    @GET("users/{username}/following")
    fun getFollowingUser(@Path("username") username: String,
                         @Header("Authorization") token: String): Call<ArrayList<MiniUserModel>>
}