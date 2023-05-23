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
import com.example.newsapp.model.News
import com.example.newsapp.R


class MyAdapter(val context: Context) : RecyclerView.Adapter<MyAdapter.NewsViewHolder>(){
    private var list = mutableListOf<News>()
    private var actionUpdate: ((News) -> Unit)? = null
    private var actionDelete: ((News) -> Unit)? = null
    private var onClickItem: ((News) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = list[position]

        holder.tvHeading.text = news.heading
        holder.title_image.setImageURI(Uri.parse("android.resource://com.example.newsapp/"+R.drawable.b))
        holder.tvPublisher.text = news.publisher
        holder.tvDate.text = news.date

        Glide.with(context).load(news.titleImage).into(holder.title_image)

        holder.actionUpdate.setOnClickListener{actionUpdate?.invoke(news)}
        holder.actionDelete.setOnClickListener{actionDelete?.invoke(news)}
        holder.itemView.setOnClickListener{onClickItem?.invoke(news)}
    }

    override fun getItemCount() = list.size

    fun setData(data: List<News>){
        list.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (News) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnActionEditListener(callback: (News) -> Unit){
        this.actionUpdate = callback
    }

    fun setOnActionDeleteListener(callback: (News) -> Unit){
        this.actionDelete = callback
    }

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title_image = itemView.findViewById<ImageView>(R.id.title_image)
        val tvHeading = itemView.findViewById<TextView>(R.id.tvHeading)
        val tvPublisher = itemView.findViewById<TextView>(R.id.tvPublisher)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val actionUpdate: ImageView = itemView.findViewById(R.id.btnUpdate)
        val actionDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }
}