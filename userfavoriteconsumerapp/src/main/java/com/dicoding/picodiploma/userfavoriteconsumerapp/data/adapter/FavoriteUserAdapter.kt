package com.dicoding.picodiploma.userfavoriteconsumerapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.userfavoriteconsumerapp.data.model.UserFavModel
import com.dicoding.picodiploma.userfavoriteconsumerapp.databinding.MiniItemUserBinding

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteHolder>() {
    var listFav = ArrayList<UserFavModel>()
        set(listFav) {
            this.listFav.clear()
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    inner class FavoriteHolder(private val binding: MiniItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFavModel) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(user.avatarUrl)
                        .apply(RequestOptions().override(350, 550))
                        .into(imgPhoto)

                txtUsername.text = user.username
            }
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
}