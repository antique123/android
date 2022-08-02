package com.sesac.constraintlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopRecyclerViewAdapter() : RecyclerView.Adapter<TopRecyclerViewAdapter.ViewHolder>() {

    val buttonTextList: MutableList<String> = mutableListOf()
    lateinit var onClickListener: (Int) -> Unit

    inner class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder =  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.top_item, parent, false))

        viewHolder.layout.setOnClickListener {
            onClickListener(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layout.findViewById<TextView>(R.id.chip_button).text = buttonTextList[position]
    }

    override fun getItemCount(): Int = buttonTextList.size

    fun clearItem() {
        buttonTextList.clear()
        notifyDataSetChanged()
    }
}