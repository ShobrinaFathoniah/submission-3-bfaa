package com.dicoding.picodiploma.githubusers.ui.userdetail.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.FavoriteUserHorizontalAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.UserFavHorizontalBinding
import com.dicoding.picodiploma.githubusers.db.DatabaseContract
import com.dicoding.picodiploma.githubusers.helper.MappingHelper
import com.dicoding.picodiploma.githubusers.ui.userdetail.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class UserFavoriteHorizontalFragment : Fragment(R.layout.user_fav_horizontal) {

    private var _binding: UserFavHorizontalBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteUserHorizontalAdapter

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = UserFavHorizontalBinding.bind(view)

        favoriteAdapter = FavoriteUserHorizontalAdapter()
        favoriteAdapter.notifyDataSetChanged()

        with(binding) {
            rvUsersFavorite.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvUsersFavorite.setHasFixedSize(true)
            rvUsersFavorite.adapter = favoriteAdapter
        }

        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFavs = deferredFavs.await()
            if (userFavs.size > 0) {
                favoriteAdapter.notifyDataSetChanged()
                favoriteAdapter.listFav = userFavs
            } else {
                favoriteAdapter.listFav = ArrayList()
            }
        }
        clickData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_PERSON, favoriteAdapter.listFav)
    }

    private fun clickData() {
        favoriteAdapter.setOnItemClickCallback(object : FavoriteUserHorizontalAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MiniUserModel) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: MiniUserModel) {
        val userData = MiniUserModel(
                user.username,
                user.avatarUrl
        )
        val moveToUserDetails = Intent(activity, UserDetails::class.java)
        moveToUserDetails.putExtra(EXTRA_PERSON, userData)
        startActivity(moveToUserDetails)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}