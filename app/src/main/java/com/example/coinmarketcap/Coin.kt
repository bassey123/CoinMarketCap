package com.example.coinmarketcap

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Coin {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "percentChange1h")
    var percent: String? = null
    @ColumnInfo(name = "priceUsd")
    var priceUsd: String? = null
    @ColumnInfo(name = "symbol")
    var symbol: String? = null
}