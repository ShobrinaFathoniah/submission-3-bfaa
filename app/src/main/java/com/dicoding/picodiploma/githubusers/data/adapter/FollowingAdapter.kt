package com.dicoding.picodiploma.githubusers.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.MiniItemUserBinding

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.UserHolder>() {
    private val list = ArrayList<MiniUserModel>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setFollowingList(user: ArrayList<MiniUserModel>) {
        list.clear()
        list.addAll(user)
        notifyDataSetChanged()
    }

    inner class UserHolder(private val binding: MiniItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: MiniUserModel) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(user.avatarUrl)
                        .apply(RequestOptions().override(350, 550))
                        .into(imgPhoto)

                txtUsername.text = user.username
            }
            itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = MiniItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MiniUserModel)
    }

}