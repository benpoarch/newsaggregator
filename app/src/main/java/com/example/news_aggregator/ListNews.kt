package com.example.news_aggregator

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.Adapter.ListNewsAdapter
import com.example.news_aggregator.Common.Common
import com.example.news_aggregator.Interface.NewsService
import com.example.news_aggregator.Model.News
import com.example.news_aggregator.Remote.NewsDetail
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListNews : AppCompatActivity() {

    var source:String?=""
    var webHotUrl:String?=""

    lateinit var dialog: AlertDialog
    lateinit var mService: NewsService
    lateinit var adapter: ListNewsAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        //init view
        mService = Common.newsService
        dialog = SpotsDialog(this)
        swipe_refresh_action.setOnRefreshListener { loadNews(source,true) }

        diagonalLayout.setOnClickListener{
            val detail = Intent(baseContext, NewsDetail::class.java)
            detail.putExtra("webURL",webHotUrl)
            startActivity(detail)
        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager = LinearLayoutManager(this)


        if(intent != null) {
            source = intent.getStringExtra("source")
            if(!source!!.isEmpty()) {
                loadNews(source,false)
            }
        }
    }

    private fun loadNews(source: String?, isRefreshed: Boolean) {

        if(isRefreshed) {
            dialog.show()
            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News> {
                    override fun onResponse(call: Call<News>, response: Response<News>) {
                        dialog.dismiss()

                        Picasso.with(baseContext).load(response.body()!!
                            .articles!![0].urlToImage).into(top_image)

                        top_title.text = response!!.body()!!.articles!![0].title
                        top_author.text = response!!.body()!!.articles!![0].author

                        webHotUrl = response!!.body()!!.articles!![0].url

                        //load remaining articles
                        val removeFirstItem = response!!.body()!!.articles
                        removeFirstItem!!.removeAt(0)

                        adapter = ListNewsAdapter(removeFirstItem!!,baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter

                    }

                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
        } else {
            swipe_refresh_action.isRefreshing = true
            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News>{
                    override fun onResponse(call: Call<News>, response: Response<News>) {
                        swipe_refresh_action.isRefreshing =false

                        Picasso.with(baseContext).load(
                            response.body()!!
                                .articles!![0].urlToImage
                        ).into(top_image)

                        top_title.text = response!!.body()!!.articles!![0].title
                        top_author.text = response!!.body()!!.articles!![0].author

                        webHotUrl = response!!.body()!!.articles!![0].url

                        //load remaining articles
                        val removeFirstItem = response!!.body()!!.articles
                        removeFirstItem!!.removeAt(0)

                        adapter = ListNewsAdapter(removeFirstItem!!, baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter

                    }

                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }

    }
}