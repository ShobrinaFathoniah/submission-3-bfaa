package com.dicoding.picodiploma.githubusers.data.model.response

import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel

data class SearchResponse(
        val items: ArrayList<MiniUserModel>
)