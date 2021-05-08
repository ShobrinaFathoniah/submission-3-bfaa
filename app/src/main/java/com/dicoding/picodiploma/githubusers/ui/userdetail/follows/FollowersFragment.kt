package com.dicoding.picodiploma.githubusers.ui.userdetail.follows

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.FollowersAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.FragmentFollowersBinding
import com.dicoding.picodiploma.githubusers.ui.userdetail.UserDetails
import com.dicoding.picodiploma.githubusers.ui.userdetail.follows.viewmodel.FollowersViewModel

@Suppress("UNREACHABLE_CODE")
class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val followersViewModel: FollowersViewModel by viewModels()
    private lateinit var username: String
    private lateinit var followersAdapter: FollowersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(UserDetails.EXTRA_USERNAME).toString()

        _binding = FragmentFollowersBinding.bind(view)

        followersAdapter = FollowersAdapter()

        with(binding) {
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.setHasFixedSize(true)
            rvFollowers.adapter = followersAdapter
        }

        followersViewModel.getUserFollowers(username)

        followersViewModel.getFollowersData().observe(viewLifecycleOwner, {
            if (it != null) {
                followersAdapter.setFollowersList(it)
                clickData()
            } else {
                Toast.makeText(activity, "Data Failed to Load", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickData() {
        followersAdapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback {
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
        moveToUserDetails.putExtra(UserDetails.EXTRA_PERSON, userData)
        startActivity(moveToUserDetails)
    }
}