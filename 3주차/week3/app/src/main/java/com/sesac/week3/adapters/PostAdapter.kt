package com.sesac.week3.adapters

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.week3.models.Post
import com.sesac.week3.databinding.PostItemBinding
import com.sesac.week3.models.Goods

class PostAdapter(private val displayMetrics: DisplayMetrics, private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var items = mutableListOf<Post>()
    lateinit var onLongClickListener: (Int) -> Unit
    lateinit var onClickListener: (Int) -> Unit

    inner class ViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            initialize()
            binding.bodyTextView.setText(items[position].body)
            binding.categorySignatureTextView.setText(items[position].categorySignature)
            binding.categoryTextView.setText(items[position].category)

            when(items[position].uris.size) {
                0 -> { }
                1 -> {
                    binding.firstImageRoot.visibility = View.VISIBLE

                    val cardViewMarginParams = binding.firstImageRoot.layoutParams as ViewGroup.MarginLayoutParams
                    cardViewMarginParams.marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, displayMetrics).toInt()

                    Glide.with(binding.firstImageView.context).load(items[position].uris[0]).into(binding.firstImageView)

                    binding.firstImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                }
                2 -> {
                    binding.firstImageRoot.visibility = View.VISIBLE
                    binding.secondImageRoot.visibility = View.VISIBLE

                    val cardViewLayoutParams = binding.secondImageRoot.layoutParams
                    cardViewLayoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160f, displayMetrics).toInt()

                    Glide.with(binding.firstImageView.context).load(items[position].uris[0]).into(binding.firstImageView)
                    Glide.with(binding.secondImageView.context).load(items[position].uris[1]).into(binding.secondImageView)

                    binding.firstImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.secondImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                }
                3 -> {
                    binding.firstImageRoot.visibility = View.VISIBLE
                    binding.secondImageRoot.visibility = View.VISIBLE
                    binding.thirdImageRoot.visibility = View.VISIBLE

                    Glide.with(binding.firstImageView.context).load(items[position].uris[0]).into(binding.firstImageView)
                    Glide.with(binding.secondImageView.context).load(items[position].uris[1]).into(binding.secondImageView)
                    Glide.with(binding.thirdImageView.context).load(items[position].uris[2]).into(binding.thirdImageView)

                    binding.firstImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.secondImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.thirdImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                }
                else -> { }
            }
        }

        private fun initialize() {
            binding.firstImageRoot.visibility = View.GONE
            binding.secondImageRoot.visibility = View.GONE
            binding.thirdImageRoot.visibility = View.GONE

            binding.firstImageView.setImageDrawable(null)
            binding.secondImageView.setImageDrawable(null)
            binding.thirdImageView.setImageDrawable(null)

            val cardViewMarginParams = binding.firstImageRoot.layoutParams as ViewGroup.MarginLayoutParams
            cardViewMarginParams.marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, displayMetrics).toInt()

            val cardViewLayoutParams = binding.secondImageRoot.layoutParams
            cardViewLayoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, displayMetrics).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun update(posts: MutableList<Post>) {
        if(items.size < posts.size)
            add(posts)
        else
            remove(posts)
    }

    private fun add(posts: MutableList<Post>) {
        val size = posts.size - items.size
        items = posts
        notifyItemRangeInserted(0, size)
    }

    private fun remove(posts: MutableList<Post>) {
        items = posts
        notifyDataSetChanged()
    }
}