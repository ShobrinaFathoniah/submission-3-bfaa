package com.dicoding.picodiploma.userfavoriteconsumerapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserFavModel(
        val username: String,
        val avatarUrl: String
) : Parcelable