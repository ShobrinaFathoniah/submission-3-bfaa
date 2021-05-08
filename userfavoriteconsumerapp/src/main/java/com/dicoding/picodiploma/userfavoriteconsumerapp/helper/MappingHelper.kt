package com.dicoding.picodiploma.userfavoriteconsumerapp.helper

import android.database.Cursor
import com.dicoding.picodiploma.userfavoriteconsumerapp.data.model.UserFavModel
import com.dicoding.picodiploma.userfavoriteconsumerapp.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(favsCursor: Cursor?): ArrayList<UserFavModel> {
        val userFavList = ArrayList<UserFavModel>()
        favsCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATARURL))
                userFavList.add(UserFavModel(username, avatarUrl))
            }
        }
        return userFavList
    }
}