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
import com.example.newsapp.R
import com.example.newsapp.model.News

class FavoriteNewsAdapter(val news: News) : RecyclerView.Adapter<FavoriteNewsAdapter.NewsViewHolder>() {
    private var list = mutableListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fav_list, parent, false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = list[position]

        holder.tvHeading.text = news.heading
        holder.title_image.setImageURI(Uri.parse("android.resource://com.example.newsapp/"+ R.drawable.b))
        holder.tvPublisher.text = news.publisher
        holder.tvDate.text = news.date

        //Glide.with(this).load(news.titleImage).into(holder.title_image)
    }

    override fun getItemCount() = list.size

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title_image = itemView.findViewById<ImageView>(R.id.title_image)
        val tvHeading = itemView.findViewById<TextView>(R.id.tvHeading)
        val tvPublisher = itemView.findViewById<TextView>(R.id.tvPublisher)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
    }
}