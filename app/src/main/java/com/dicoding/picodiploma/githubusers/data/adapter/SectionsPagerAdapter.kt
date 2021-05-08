package com.dicoding.picodiploma.githubusers.data.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.picodiploma.githubusers.ui.userdetail.follows.FollowersFragment
import com.dicoding.picodiploma.githubusers.ui.userdetail.follows.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val data: Bundle) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.data
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}