package com.example.news_aggregator

class MyNews {
    var newsName: String? = null
    private var newsImage: Int = 0

    fun getNames(): String {
        return newsName.toString()
    }

    fun setNames(name: String) {
        this.newsName = name
    }

    fun getImages(): Int {
        return newsImage
    }

    fun setImages(image_drawable: Int) {
        this.newsImage = image_drawable
    }
}