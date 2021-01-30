package com.example.news_aggregator.Interface

import android.provider.ContactsContract
import com.example.news_aggregator.Model.News
import com.example.news_aggregator.Model.WebSite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @get:GET("v2/sources?apiKey=86c770f6d9f24e79be20662366150242")
    val sources: Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url:String) :Call<News>
}