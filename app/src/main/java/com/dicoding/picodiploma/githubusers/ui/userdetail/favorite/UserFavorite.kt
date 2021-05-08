package com.dicoding.picodiploma.githubusers.ui.userdetail.favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.FavoriteUserAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.ActivityUserFavBinding
import com.dicoding.picodiploma.githubusers.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubusers.helper.MappingHelper
import com.dicoding.picodiploma.githubusers.ui.userdetail.UserDetails
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class UserFavorite : AppCompatActivity() {
    private lateinit var binding: ActivityUserFavBinding
    private lateinit var favoriteAdapter: FavoriteUserAdapter

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteAdapter = FavoriteUserAdapter()

        supportActionBar?.title = "User Favorite"
        loading(status = false)

        with(binding) {
            rvUsers.layoutManager = LinearLayoutManager(this@UserFavorite)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = favoriteAdapter
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                favoriteAdapter.notifyDataSetChanged()
                loadFavsAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            favoriteAdapter.notifyDataSetChanged()
            loadFavsAsync()
        } else {
            favoriteAdapter.notifyDataSetChanged()
            savedInstanceState.getParcelableArrayList<MiniUserModel>(EXTRA_PERSON)?.also { favoriteAdapter.listFav = it }
        }

        contentResolver

        clickData()
    }

    private fun loading(status: Boolean) {
        binding.loadingBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun loadFavsAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            loading(true)
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFavs = deferredFavs.await()
            loading(false)
            if (userFavs.size > 0) {
                favoriteAdapter.notifyDataSetChanged()
                favoriteAdapter.listFav = userFavs
            } else {
                favoriteAdapter.listFav = ArrayList()
                showSnackBarMessage(getString(R.string.message_no_data))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_PERSON, favoriteAdapter.listFav)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvUsers, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun clickData() {
        favoriteAdapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
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
        val moveToUserDetails = Intent(this@UserFavorite, UserDetails::class.java)
        moveToUserDetails.putExtra(EXTRA_PERSON, userData)
        startActivity(moveToUserDetails)
    }
}