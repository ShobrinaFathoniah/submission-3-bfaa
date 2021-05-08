package com.dicoding.picodiploma.githubusers.ui.userdetail.follows

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.adapter.FollowingAdapter
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.FragmentFollowingBinding
import com.dicoding.picodiploma.githubusers.ui.userdetail.UserDetails
import com.dicoding.picodiploma.githubusers.ui.userdetail.follows.viewmodel.FollowingViewModel

@Suppress("UNREACHABLE_CODE")
class FollowingFragment : Fragment(R.layout.fragment_following) {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String
    private lateinit var followingAdapter: FollowingAdapter
    private val followingViewModel: FollowingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(UserDetails.EXTRA_USERNAME).toString()
        _binding = FragmentFollowingBinding.bind(view)
        followingAdapter = FollowingAdapter()

        with(binding) {
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.setHasFixedSize(true)
            rvFollowing.adapter = followingAdapter
        }

        followingViewModel.getUserFollowing(username)
        followingViewModel.getFollowingData().observe(viewLifecycleOwner, {
            if (it != null) {
                followingAdapter.setFollowingList(it)
                clickData()
            } else {
                Toast.makeText(activity, "Data Failed to Load", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clickData() {
        followingAdapter.setOnItemClickCallback(object : FollowingAdapter.OnItemClickCallback {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}