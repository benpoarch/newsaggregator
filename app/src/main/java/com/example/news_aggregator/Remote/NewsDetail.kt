package com.example.news_aggregator.Remote

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import android.app.AlertDialog
import dmax.dialog.SpotsDialog
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_news_detail.*


class NewsDetail : AppCompatActivity() {

    lateinit var alertDialog:AlertDialog


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_news_detail)

        alertDialog = SpotsDialog(this)
        alertDialog.show()

        webView.settings.javaScriptEnabled=true
        webView.webChromeClient=WebChromeClient()
        webView.webViewClient=object:WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                alertDialog.dismiss()
            }
        }

        if(intent!=null){
            if(!intent.getStringExtra("webURL")!!.isEmpty()) {
                webView.loadUrl(intent.getStringExtra("webURL")!!)
            }
        }
    }
}