package com.example.news_aggregator.Common

import com.example.news_aggregator.Interface.NewsService
import com.example.news_aggregator.Remote.RetrofitClient
import java.lang.StringBuilder

object Common {
    val BASE_URL = "https://newsapi.org/"
    val API_KEY = "86c770f6d9f24e79be20662366150242"

    val newsService:NewsService
        get()=RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)


    fun getNewsAPI(source:String):String{
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
            .append(source).append("&apiKey=").append(API_KEY).toString()
        return apiUrl
    }
}