package com.example.newsapp.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.CategoryButton
import kotlinx.coroutines.currentCoroutineContext

class ButtonListAdapter(protected val categoryList: ArrayList<CategoryButton>): RecyclerView.Adapter<ButtonListAdapter.ButtonViewHolder>(){
    private lateinit var mListener: OnClickItemListener
    var category : ArrayList<CategoryButton>? = null

    interface OnClickItemListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnClickItemListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_list, parent, false)

        return ButtonViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.index = position

        if (currentItem.isChecked){
            holder.buttonTitle.setBackgroundResource(R.drawable.bg_categorybtn_primary)
            holder.buttonTitle.setTextColor(Color.parseColor("white"))
        }
        else{
            holder.buttonTitle.setBackgroundResource(R.drawable.bg_categorybtn_white)
            holder.buttonTitle.setTextColor(Color.parseColor("black"))
        }

        holder.buttonTitle.text = currentItem.button_title
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun getData(position: Int): CategoryButton{
        return categoryList[position]
    }

    class ButtonViewHolder (itemView: View, listener: OnClickItemListener): RecyclerView.ViewHolder(itemView){
        val buttonTitle: TextView = itemView.findViewById(R.id.category_name)
        var index: Int = 0

        init {
            itemView.setOnClickListener{
                listener.onItemClick(index)
            }
        }
    }
}