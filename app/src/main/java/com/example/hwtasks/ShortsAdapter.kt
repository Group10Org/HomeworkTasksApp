package com.example.hwtasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hwtasks.R

class ShortsAdapter(
    private val items: MutableList<ShortItem>,
    private val onClick: (ShortItem) -> Unit
) : RecyclerView.Adapter<ShortsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.shortThumb)
        val title: TextView = view.findViewById(R.id.shortTitle)
        val channel: TextView = view.findViewById(R.id.shortChannel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_short, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.thumbnailUrl)
            .into(holder.thumbnail)

        holder.title.text = item.title
        holder.channel.text = item.channelName

        holder.itemView.setOnClickListener { onClick(item) }
    }

    fun addMore(list: List<ShortItem>) {
        val start = items.size
        items.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }
}