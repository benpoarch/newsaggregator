package com.example.news_aggregator.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.news_aggregator.Interface.ItemClickListener
import kotlinx.android.synthetic.main.news_layout.view.*

class ListNewsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener

    var article_title = itemView.article_title
    var article_time = itemView.article_time
    var article_image = itemView.article_image

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        itemClickListener.onClick(v,adapterPosition)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener=itemClickListener
    }
}