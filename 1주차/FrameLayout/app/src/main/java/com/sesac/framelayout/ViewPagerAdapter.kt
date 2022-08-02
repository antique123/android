package com.sesac.framelayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ViewPagerAdapter(private val imageUrls: MutableList<String>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {
        private val imageView = layout.findViewById<ImageView>(R.id.full_screen_image_view)

        fun bind(position: Int) {
            Glide.with(imageView.context)
                .load(imageUrls[position])
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = imageUrls.size
}