package com.example.coinmarketcap

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create instance for AppDatabase class
        appDatabase = AppDatabase.getInstance(this@MainActivity)

        // object of coin class
        val coin = Coin()

        val url = "https://api.coinmarketcap.com/v1/ticker/?limit=50"

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val dataList = ArrayList<MainModel>()
        val adapter = MainAdapter(dataList)
        recyclerView.adapter = adapter

        /*val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true*/


        fun Context.isConnectedToNetwork(): Boolean {
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
        }

        if (applicationContext.isConnectedToNetwork()) {
            val pd = ProgressDialog(this@MainActivity)
            pd.setMessage("Loading...")
            pd.show()

            var i = 0

            val req = JsonArrayRequest(
                url,
                Response.Listener {
                    while (i < it.length()) {
                        try {
                            val jsonObject = it.getJSONObject(i)
                            val mainModel = MainModel(
                                jsonObject.getString("name"),
                                jsonObject.getString("percent_change_1h"),
                                jsonObject.getString("price_usd"),
                                jsonObject.getString("symbol")
                            )
                            dataList.add(mainModel)
                            // setting coin data to be added to the list
                            coin.name = mainModel.name
                            coin.percent = mainModel.percentChange1h
                            coin.priceUsd = mainModel.priceUsd
                            coin.symbol = mainModel.symbol
                            // thread for saving coin data
                            Thread {
                                appDatabase.coinDao().saveCoin(coin)
                            }.start()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            pd.dismiss()
                        }
                        adapter.notifyDataSetChanged()
                        pd.dismiss()
                        i++
                    }
                }, Response.ErrorListener {
                    Log.e("Volley", it.toString())
                    pd.dismiss()
                }
            )
            queue.add(req)
        } else {
            // thread for retrieving coin data
            Thread {
                appDatabase.coinDao().loadAll().forEach {
                    runOnUiThread {
                        val mainModel = MainModel(
                            it.name!!,
                            it.percent!!,
                            it.priceUsd!!,
                            it.symbol!!
                        )
                        dataList.add(mainModel)
                        adapter.notifyDataSetChanged()
                    }
                }
            }.start()
        }

    }
}
