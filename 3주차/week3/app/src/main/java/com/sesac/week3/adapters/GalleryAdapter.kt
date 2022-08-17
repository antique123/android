package com.sesac.week3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.week3.R
import com.sesac.week3.databinding.GalleryItemBinding

class GalleryAdapter(private val uris: MutableList<String>, private val context: Context) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private var selectedPositions = mutableListOf<Int>()
    private var selectedUris = mutableListOf<String>()


    inner class ViewHolder(val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if(selectedPositions.contains(position)) {
                binding.galleryItem.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.orange
                ))
            } else {
                binding.galleryItem.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.white
                ))
            }

            Glide.with(binding.photoImageView.context)
                .load(uris[position])
                .into(binding.photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            Toast.makeText(context, "${viewHolder.adapterPosition} 아이템 클릭됨", Toast.LENGTH_SHORT).show()
            if(selectedUris.size >= 3 && !selectedUris.contains(uris[viewHolder.adapterPosition])) {
                Toast.makeText(context, "최대 3개의 이미지를 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(selectedUris.contains(uris[viewHolder.adapterPosition])) {
                selectedUris.remove(uris[viewHolder.adapterPosition])
                selectedPositions.remove(viewHolder.adapterPosition)
                viewHolder.binding.galleryItem.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.white
                ))
            } else {
                selectedUris.add(uris[viewHolder.adapterPosition])
                selectedPositions.add(viewHolder.adapterPosition)
                viewHolder.binding.galleryItem.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.orange
                ))
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = uris.size

    fun getSelectedUris() = selectedUris
}