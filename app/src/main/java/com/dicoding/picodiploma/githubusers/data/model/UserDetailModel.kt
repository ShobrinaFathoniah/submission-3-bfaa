package com.dicoding.picodiploma.githubusers.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailModel(
        @SerializedName("avatar_url")
        val avatarUrl: String?,
        @SerializedName("login")
        val username: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("company")
        val company: String?,
        @SerializedName("location")
        val location: String?,
        @SerializedName("public_repos")
        val repository: String?,
        @SerializedName("followers")
        val followers: String?,
        @SerializedName("following")
        val following: String?
) : Parcelable
