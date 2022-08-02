package com.sesac.constraintlayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.padding

class RecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    val itemTextModelList: MutableList<Model> = mutableListOf()
    lateinit var onItemClickListener: (Int) -> Unit
    lateinit var onLikeItemClickListener: (Int) -> Unit
    lateinit var onWriteItemClickListener: (Int) -> Unit

    inner class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

        viewHolder.layout.findViewById<ConstraintLayout>(R.id.outer_layout).setOnClickListener {
            onItemClickListener(viewHolder.adapterPosition)
        }
        viewHolder.layout.findViewById<TextView>(R.id.like_button_text_view).setOnClickListener {
            onLikeItemClickListener(viewHolder.adapterPosition)
        }
        viewHolder.layout.findViewById<TextView>(R.id.write_button_text_view).setOnClickListener {
            onWriteItemClickListener(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layout.findViewById<TextView>(R.id.balloon_text_view).text = itemTextModelList[position].balloonText
        holder.layout.findViewById<TextView>(R.id.main_text_view).text = itemTextModelList[position].mainText
        holder.layout.findViewById<TextView>(R.id.write_time_text_view).text = itemTextModelList[position].writeTimeText

        balloonTextViewBugFix(holder.layout.findViewById(R.id.balloon_text_view))

    }

    override fun getItemCount(): Int = itemTextModelList.size

    fun clearItem() {
        itemTextModelList.clear()
        notifyDataSetChanged()
    }

    private fun balloonTextViewBugFix(balloonTextView: TextView) {

        fun dpToPx(dp: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }

        val layoutParams = balloonTextView.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        balloonTextView.layoutParams = layoutParams
        balloonTextView.padding = dpToPx(6)
    }

}