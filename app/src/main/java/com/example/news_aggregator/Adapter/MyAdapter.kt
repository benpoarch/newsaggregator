package com.example.news_aggregator.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.news_aggregator.Interface.ItemClickListener
import com.example.news_aggregator.ListNews
import com.example.news_aggregator.Model.WebSite
import com.example.news_aggregator.MyNews
import com.example.news_aggregator.R
import com.google.android.material.snackbar.Snackbar


public class MyAdapter(private val context:Context, private val webSite: WebSite):RecyclerView.Adapter<ListSourceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.source_news_layout,parent,false)
        return ListSourceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListSourceViewHolder, position: Int) {
        holder!!.source_title.text = webSite.sources!![position].name
        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int) {
                //TODO: open an activity showing the full article
                val intent = Intent(context,ListNews::class.java)
                intent.putExtra("source",webSite.sources!![position].id)
                context.startActivity(intent)
            }
        })
    }

    override fun getItemCount(): Int {
        return webSite.sources!!.size
    }

}
