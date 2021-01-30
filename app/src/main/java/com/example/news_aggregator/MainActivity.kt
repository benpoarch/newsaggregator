package com.example.news_aggregator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.app.AlertDialog
import android.provider.ContactsContract
import android.telecom.Call
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.Adapter.MyAdapter
import com.example.news_aggregator.Common.Common
import com.example.news_aggregator.Interface.NewsService
import com.example.news_aggregator.Model.WebSite
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var  layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: MyAdapter
    lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for menu bar
        setSupportActionBar(toolbar)

        //for api
        Paper.init(this)
        mService = Common.newsService
        my_recycler_view.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        my_recycler_view.layoutManager = layoutManager

        dialog = SpotsDialog(this)
        loadWebSiteSource(false)


    }



    //for login activity
    private fun logout() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(baseContext, "Successfully logged out.",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, "Not logged in.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            Toast.makeText(baseContext, "You are already logged in.",
                Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }


    //for toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemview = item.itemId

        when(itemview) {
            //R.id.profile_action -> Toast.makeText(applicationContext, "Profile Clicked", Toast.LENGTH_SHORT).show()
            R.id.refresh_action -> loadWebSiteSource(true)
            R.id.my_news_action -> startActivity(Intent(this@MainActivity, UserPreferencesActivity::class.java))
            R.id.profile_action -> login()
            R.id.logout_action -> logout()
        }

        return false
    }


    //for api
    private fun loadWebSiteSource(isRefresh: Boolean) {
        if(!isRefresh) {
            val cache = Paper.book().read<String>("cache")
            if(cache != null && !cache.isBlank() && cache != "null") {
                val webSite = Gson().fromJson<WebSite>(cache,WebSite::class.java)
                adapter = MyAdapter(baseContext,webSite)
                adapter.notifyDataSetChanged()
                my_recycler_view.adapter = adapter

            }
            else {
                dialog.show()
                mService.sources.enqueue(object: retrofit2.Callback<WebSite> {
                    override fun onResponse(
                        call: retrofit2.Call<WebSite>,
                        response: Response<WebSite>
                    ) {
                        adapter = MyAdapter(baseContext,response!!.body()!!)
                        adapter.notifyDataSetChanged()
                        my_recycler_view.adapter = adapter

                        Paper.book().write("cache",Gson().toJson(response!!.body()!!))

                        dialog.dismiss()
                    }

                    override fun onFailure(call: retrofit2.Call<WebSite>, t: Throwable) {
                        Toast.makeText(baseContext, "Failed",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        else {
            swipe_refresh_action.isRefreshing=true

            mService.sources.enqueue(object: retrofit2.Callback<WebSite> {
                override fun onResponse(
                    call: retrofit2.Call<WebSite>,
                    response: Response<WebSite>
                ) {
                    adapter = MyAdapter(baseContext,response!!.body()!!)
                    adapter.notifyDataSetChanged()
                    my_recycler_view.adapter = adapter

                    Paper.book().write("cache",Gson().toJson(response!!.body()!!))

                    swipe_refresh_action.isRefreshing=false
                }
                override fun onFailure(call: retrofit2.Call<WebSite>, t: Throwable) {
                    Toast.makeText(baseContext, "Failed",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}