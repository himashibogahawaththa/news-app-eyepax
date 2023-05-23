package com.example.newsapp.view.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.model.LatestNews
import com.example.newsapp.R

class HorizontalRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<HorizontalRecyclerViewAdapter.LatestNewsHolder> (){
    private val list = mutableListOf<LatestNews>()
    private var onClickItem: ((LatestNews) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestNewsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_list, parent, false)
        return LatestNewsHolder(view)
    }

    override fun onBindViewHolder(holder: LatestNewsHolder, position: Int) {
        val dataset = list[position]

        holder.hrPublisher.text = dataset.publisher
        holder.hrDescription.text = dataset.description
        holder.hrHeadline.text = dataset.heading
        holder.hrImage.setImageURI(Uri.parse("android.resource://com.example.newsapp/"+ R.drawable.a))

        Glide.with(context).load(dataset.titleImage).into(holder.hrImage)

        holder.itemView.setOnClickListener{onClickItem?.invoke(dataset)}
    }

    override fun getItemCount() = list.size

    class LatestNewsHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val hrImage = itemView.findViewById<ImageView>(R.id.title_image)!!
        val hrHeadline = itemView.findViewById<TextView>(R.id.tvHeading)!!
        val hrPublisher = itemView.findViewById<TextView>(R.id.tvPublisher)!!
        val hrDescription = itemView.findViewById<TextView>(R.id.tvDescription)!!
    }

    fun setData(data: List<LatestNews>) {
        list.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (LatestNews) -> Unit) {
        this.onClickItem = callback
    }
}