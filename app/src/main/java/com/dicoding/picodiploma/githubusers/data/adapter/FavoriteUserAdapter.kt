package com.dicoding.picodiploma.githubusers.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubusers.data.model.MiniUserModel
import com.dicoding.picodiploma.githubusers.databinding.MiniItemUserBinding

class FavoriteUserAdapter() : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteHolder>() {
    var listFav = ArrayList<MiniUserModel>()
        set(listFav) {
            this.listFav.clear()
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class FavoriteHolder(private val binding: MiniItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = MiniItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int {
        return listFav.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MiniUserModel)
    }
}