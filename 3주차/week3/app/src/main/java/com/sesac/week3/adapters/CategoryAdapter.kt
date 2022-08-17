package com.sesac.week3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sesac.week3.databinding.CategoryItemBinding

class CategoryAdapter(private val categories: MutableList<String>, private val context: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.categoryButton.text = categories[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.category.setOnClickListener {
            Toast.makeText(context, "${viewHolder.adapterPosition} item 클릭됨", Toast.LENGTH_SHORT).show()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = categories.size
}