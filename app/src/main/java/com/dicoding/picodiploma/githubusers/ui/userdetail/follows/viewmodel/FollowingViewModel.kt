package com.dicoding.picodiploma.githubusers.ui.userdetail.follows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.data.network.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _miniDetailFollowing = MutableLiveData<ArrayList<MiniUserModel>>()

    fun getUserFollowing(username: String) {
        ApiClient.instance
                .getFollowingUser(username, "5b9178222bb5c0b32a10711743dcf46ad0c534a6")
                .enqueue(object : Callback<ArrayList<MiniUserModel>> {
                    override fun onResponse(
                            call: Call<ArrayList<MiniUserModel>>,
                            response: Response<ArrayList<MiniUserModel>>
                    ) {
                        _miniDetailFollowing.postValue(response.body())
                    }

                    override fun onFailure(call: Call<ArrayList<MiniUserModel>>, t: Throwable) {
                    }

                })
    }

    fun getFollowingData(): LiveData<ArrayList<MiniUserModel>> {
        return _miniDetailFollowing
    }
}