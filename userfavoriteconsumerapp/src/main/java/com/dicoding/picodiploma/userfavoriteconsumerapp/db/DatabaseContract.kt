package com.dicoding.picodiploma.userfavoriteconsumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.picodiploma.githubusers"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {

        companion object {
            private const val TABLE_NAME = "user_fav"
            const val USERNAME = "login"
            const val AVATARURL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}