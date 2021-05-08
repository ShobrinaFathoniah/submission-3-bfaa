package com.dicoding.picodiploma.githubusers.ui.userdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.githubusers.data.model.UserDetailModel
import com.dicoding.picodiploma.githubusers.data.network.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    val detailUser = MutableLiveData<UserDetailModel>()

    fun getUserDetail(username: String) {
        ApiClient.instance
                .getDetailUser(username, "5b9178222bb5c0b32a10711743dcf46ad0c534a6")
                .enqueue(object : Callback<UserDetailModel> {
                    override fun onResponse(
                            call: Call<UserDetailModel>,
                            response: Response<UserDetailModel>
                    ) {
                        detailUser.postValue(response.body())
                    }

                    override fun onFailure(call: Call<UserDetailModel>, t: Throwable) {
                    }

                })
    }

    fun getDataDetailUser(): LiveData<UserDetailModel> {
        return detailUser
    }
}