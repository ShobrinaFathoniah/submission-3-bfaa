package com.dicoding.picodiploma.userfavoriteconsumerapp.ui.favorite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.userfavoriteconsumerapp.R
import com.dicoding.picodiploma.userfavoriteconsumerapp.data.adapter.FavoriteUserAdapter
import com.dicoding.picodiploma.userfavoriteconsumerapp.data.model.UserFavModel
import com.dicoding.picodiploma.userfavoriteconsumerapp.databinding.ActivityUserFavBinding
import com.dicoding.picodiploma.userfavoriteconsumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.userfavoriteconsumerapp.helper.MappingHelper
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
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer User Favorite"
        loading(status = false)

        favoriteAdapter = FavoriteUserAdapter()
        favoriteAdapter.notifyDataSetChanged()

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
                loadFavsAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadFavsAsync()
        } else {
            savedInstanceState.getParcelableArrayList<UserFavModel>(EXTRA_STATE)?.also { favoriteAdapter.listFav = it }
        }
    }

    private fun loading(status: Boolean) {
        binding.loadingBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun loadFavsAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFavs = deferredFavs.await()
            if (userFavs.size > 0) {
                favoriteAdapter.listFav = userFavs
            } else {
                favoriteAdapter.listFav = ArrayList()
                showSnackBarMessage(getString(R.string.message_no_data))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.listFav)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvUsers, message, Snackbar.LENGTH_SHORT).show()
    }
}