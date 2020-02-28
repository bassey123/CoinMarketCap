package com.example.coinmarketcap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter (
    private val dataList: ArrayList<MainModel>
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var price: TextView = itemView.findViewById(R.id.price_usd)
        var percent: TextView = itemView.findViewById(R.id.percent_change_1h)
        var symbol: TextView = itemView.findViewById(R.id.symbol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.name.text = item.name
        holder.percent.text = item.percentChange1h
        holder.price.text = item.priceUsd
        holder.symbol.text = item.symbol
    }
}