package com.example.tastywardoffice.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tastywardoffice.databinding.GridViewItemBinding
import com.example.tastywardoffice.network.TastyPhoto
import com.example.tastywardoffice.restaurant_listDirections
import com.google.android.gms.maps.model.LatLng

class PhotoGridAdapter : ListAdapter<TastyPhoto,
        PhotoGridAdapter.TastyPhotoViewHolder>(DiffCallback) {

    class TastyPhotoViewHolder(private var binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(TastyPhoto: TastyPhoto) {
            binding.photo = TastyPhoto
            binding.executePendingBindings()

        }
        val ImageView = binding.menuImage
        val TextView = binding.nameStore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TastyPhotoViewHolder {
        return TastyPhotoViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TastyPhotoViewHolder, position: Int) {
        val tastyPhoto = getItem(position)
        holder.bind(tastyPhoto)

        holder.itemView.setOnClickListener {
            val action = restaurant_listDirections.actionRestaurantListToDetailMenu3(
                storename = holder.TextView.text.toString(),
                imgUri = tastyPhoto.imgSrcUrl,
                LatLng(37.56, 126.97)
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TastyPhoto>() {
        override fun areItemsTheSame(oldItem: TastyPhoto, newItem: TastyPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TastyPhoto, newItem: TastyPhoto): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

    }

}