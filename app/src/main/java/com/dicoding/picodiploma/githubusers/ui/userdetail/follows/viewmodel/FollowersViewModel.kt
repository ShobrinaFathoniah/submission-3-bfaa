package com.dicoding.picodiploma.githubusers.ui.userdetail.follows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.data.network.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _miniDetailFollowers = MutableLiveData<ArrayList<MiniUserModel>>()

    fun getUserFollowers(username: String) {
        ApiClient.instance
                .getFollowersUser(username, "TOKEN GITHUB")
                .enqueue(object : Callback<ArrayList<MiniUserModel>> {
                    override fun onResponse(
                            call: Call<ArrayList<MiniUserModel>>,
                            response: Response<ArrayList<MiniUserModel>>
                    ) {
                        _miniDetailFollowers.postValue(response.body())
                    }

                    override fun onFailure(call: Call<ArrayList<MiniUserModel>>, t: Throwable) {
                    }

                })
    }

    fun getFollowersData(): LiveData<ArrayList<MiniUserModel>> {
        return _miniDetailFollowers
    }
}
