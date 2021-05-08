package com.dicoding.picodiploma.githubusers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.data.model.response.SearchResponse
import com.dicoding.picodiploma.githubusers.data.network.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<MiniUserModel>>()

    fun getQuery(query: String) {
        ApiClient.instance
                .getSearch(query, "TOKEN GITHUB")
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                            call: Call<SearchResponse>,
                            response: Response<SearchResponse>
                    ) {
                        listUser.postValue(response.body()?.items)
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                    }

                })
    }

    fun getSearch(): LiveData<ArrayList<MiniUserModel>> {
        return listUser
    }
}

