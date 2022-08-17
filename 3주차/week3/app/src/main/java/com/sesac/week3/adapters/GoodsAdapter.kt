package com.sesac.week3.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.week3.databinding.GoodsItemBinding
import com.sesac.week3.models.Goods

class GoodsAdapter() : RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {
    var items: MutableList<Goods> = mutableListOf()
    lateinit var onLongClickListener: (Int) -> Unit
    lateinit var onClickListener: (Int) -> Unit

    inner class ViewHolder(val binding: GoodsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.goodsExplainView.text = items[position].explain
            binding.locationTextView.text = items[position].location
            binding.priceTextView.text = items[position].price

            Glide.with(binding.goodsImageView.context)
                .load(items[position].imageURL[0])
                .into(binding.goodsImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GoodsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        viewHolder.binding.root.setOnLongClickListener {
            onLongClickListener(viewHolder.adapterPosition)
            true
        }
        viewHolder.binding.root.setOnClickListener {
            onClickListener(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size


    fun update(goods: MutableList<Goods>) {
        if(items.size < goods.size)
            add(goods)
        else
            remove(goods)
    }

    private fun add(goods: MutableList<Goods>) {
        val size = goods.size - items.size
        items = goods
        notifyItemRangeInserted(0, size)
    }

    private fun remove(goods: MutableList<Goods>) {
        items = goods
        notifyDataSetChanged()
    }


    fun installFilter(filtered: MutableList<Goods>) {
        items = filtered
        notifyDataSetChanged()
    }

    fun removeFilter(origin: MutableList<Goods>) {
        items = origin
        notifyDataSetChanged()
    }

    fun modify(position: Int) {
        notifyItemChanged(position)
    }

}