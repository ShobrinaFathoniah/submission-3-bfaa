package com.dicoding.picodiploma.githubusers.helper

import android.database.Cursor
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(favsCursor: Cursor?): ArrayList<MiniUserModel> {
        val userFavList = ArrayList<MiniUserModel>()
        favsCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATARURL))
                userFavList.add(MiniUserModel(username, avatarUrl))
            }
        }
        return userFavList
    }

}