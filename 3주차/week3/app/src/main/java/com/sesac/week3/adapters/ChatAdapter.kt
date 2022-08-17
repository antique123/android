package com.sesac.week3.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.week3.databinding.ItemChatBinding
import com.sesac.week3.models.Chat
import java.util.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    var items = mutableListOf<Chat>()

    inner class ViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(binding.profileImageView.context)
                .load(items[position].profileURL)
                .circleCrop()
                .into(binding.profileImageView)

            binding.nameTextView.text = items[position].name
            binding.messageTextView.text = items[position].message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    fun addItems(items: MutableList<Chat>) {
        if(this.items.isEmpty()) {
            this.items = items
            notifyDataSetChanged()
        } else {
            this.items = items
            notifyItemInserted(0)
        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapData(dragStartPosition: Int, dragEndPosition: Int) {
        Collections.swap(items, dragStartPosition, dragEndPosition)
        notifyItemMoved(dragStartPosition, dragEndPosition)
    }

}