package com.dicoding.picodiploma.githubusers.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.UserSearchAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.MainLayoutBinding
import com.dicoding.picodiploma.githubusers.ui.setting.SettingContainer
import com.dicoding.picodiploma.githubusers.ui.userdetail.UserDetails
import com.dicoding.picodiploma.githubusers.ui.userdetail.favorite.UserFavorite
import com.dicoding.picodiploma.githubusers.ui.userdetail.favorite.UserFavoriteHorizontalFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: MainLayoutBinding

    private val searchViewModel: UserViewModel by viewModels()
    private lateinit var searchAdapter: UserSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = MainLayoutBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        loadingSearch(status = false)
        horizontalFavorite(state = true)

        searchAdapter = UserSearchAdapter()
        searchAdapter.notifyDataSetChanged()

        with(bindingMain.searchLayout) {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = searchAdapter
        }

        searchViewModel.getSearch().observe(this, {
            loadingSearch(status = false)
            if (it != null) {
                horizontalFavorite(state = false)
                searchAdapter.setList(it)
            } else {
                horizontalFavorite(state = true)
            }
        })
    }

    fun clickData() {
        searchAdapter.setOnItemClickCallback(object : UserSearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MiniUserModel) {
                loadingSearch(status = true)
                showSelectedUser(data)
                loadingSearch(status = false)
            }
        })
    }

    private fun showSelectedUser(user: MiniUserModel) {
        val userData = MiniUserModel(
            user.username,
            user.avatarUrl
        )
        val moveToUserDetails = Intent(this@MainActivity, UserDetails::class.java)
        moveToUserDetails.putExtra(UserDetails.EXTRA_PERSON, userData)
        startActivity(moveToUserDetails)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                loadingSearch(status = true)
                horizontalFavorite(state = false)
                searchViewModel.getQuery(query)
                clickData()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun loadingSearch(status: Boolean) {
        bindingMain.searchLayout.loadingBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.LanguageSetting -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.alarmSetting -> {
                val intent = Intent(this@MainActivity, SettingContainer::class.java)
                startActivity(intent)
            }
            R.id.listFavorite -> {
                val intent = Intent(this@MainActivity, UserFavorite::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun horizontalFavorite(state: Boolean) {
        if (state) {
            bindingMain.searchLayout.listSearch.visibility = View.GONE
            bindingMain.userFavHorizontal.listFavorite.visibility = View.VISIBLE
            setContentView(R.layout.favorite_container)

            val mFragmentManager = supportFragmentManager
            val mFavoriteFragment = UserFavoriteHorizontalFragment()
            val fragment = mFragmentManager.findFragmentByTag(UserFavoriteHorizontalFragment::class.java.simpleName)
            if (fragment !is UserFavoriteHorizontalFragment) {
                mFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.favorite_container,
                        mFavoriteFragment,
                        UserFavoriteHorizontalFragment::class.java.simpleName
                    )
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            bindingMain.userFavHorizontal.listFavorite.visibility = View.GONE
            bindingMain.searchLayout.listSearch.visibility = View.VISIBLE
            setContentView(bindingMain.root)
        }
    }
}