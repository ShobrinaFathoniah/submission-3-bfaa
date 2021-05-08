package com.dicoding.picodiploma.githubusers.ui.userdetail

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.ActivityUserDetailsBinding
import com.dicoding.picodiploma.githubusers.databinding.ActivityUserDetailsBinding.*
import com.dicoding.picodiploma.githubusers.db.DatabaseContract
import com.dicoding.picodiploma.githubusers.db.FavoriteHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetails : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_PERSON = "extra_person"
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var nameUser: String
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private lateinit var repositoryUser: String
    private lateinit var followerUser: String
    private lateinit var followingUser: String
    private lateinit var companyUser: String
    private lateinit var locationUser: String
    private lateinit var favoriteHelper: FavoriteHelper
    private var _isChecked: Boolean = false
    private val detailModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        loading(status = true)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val user = intent.getParcelableExtra<MiniUserModel>(EXTRA_PERSON) as MiniUserModel
        username = user.username.toString()
        avatarUrl = user.avatarUrl.toString()
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detailModel.getUserDetail(username)
        detailModel.getDataDetailUser().observe(this, {
            if (it != null) {
                with(binding) {
                    with(it) {
                        Glide.with(this@UserDetails)
                                .load(avatarUrl)
                                .apply(RequestOptions().override(350, 550))
                                .into(imgPhoto)

                        txtUsername.text = username
                        txtName.text = name
                        txtFollowers.text = followers
                        txtFollowing.text = following
                        txtCompany.text = company
                        txtLocation.text = location
                        txtRepository.text = repository

                        nameUser = name.toString()
                        companyUser = company.toString()
                        locationUser = location.toString()
                        followerUser = followers.toString()
                        followingUser = following.toString()
                        repositoryUser = repository.toString()
                        loading(status = false)
                    }
                }
            } else {
                Toast.makeText(this, "Data Failed to Load, There is null", Toast.LENGTH_SHORT).show()
            }
        })

        val cursor: Cursor = favoriteHelper.queryByUsername(username)
        if (cursor.moveToNext()) {
            _isChecked = true
            favoriteState(true)
        } else {
            _isChecked = false
            favoriteState(false)
        }

        binding.btnShare.setOnClickListener(this)
        binding.btnFollow.setOnClickListener(this)
        binding.favoriteToogle.setOnClickListener(this)
    }

    private fun favorited() {
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
        values.put(DatabaseContract.FavoriteColumns.AVATARURL, avatarUrl)

        contentResolver.insert(DatabaseContract.FavoriteColumns.CONTENT_URI, values)
        _isChecked = true
        Toast.makeText(this, "You Add $username in favorite", Toast.LENGTH_SHORT).show()
    }

    private fun unfavorite() {
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
        favoriteHelper.deleteByUsername(username)
        _isChecked = false
        Toast.makeText(this, "You Delete $username in favorite", Toast.LENGTH_SHORT).show()
    }

    private fun favoriteState(_isChecked: Boolean) {
        binding.favoriteToogle.isChecked = _isChecked
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_share -> {
                val userData = "Name: $nameUser, Username: $username, Repository: $repositoryUser, Location: $locationUser, Company: $companyUser\nLink Github: github.com/$username"
                val sendData: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, userData)
                    type = "text/plain"
                }
                val shareData = Intent.createChooser(sendData, null)
                startActivity(shareData)
            }
            R.id.btn_follow -> {
                var url = "http://www.github.com/$username"
                if (!url.startsWith("http://")) {
                    url = "http://$url"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
            R.id.favorite_toogle -> {
                if (_isChecked) {
                    unfavorite()
                } else {
                    favorited()
                }
            }
        }
    }

    private fun loading(status: Boolean) {
        binding.loadingBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}
