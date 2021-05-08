package com.dicoding.picodiploma.githubusers.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiniUserModel(
        @SerializedName("login")
        val username: String?,
        @SerializedName("avatar_url")
        val avatarUrl: String?
) : Parcelable