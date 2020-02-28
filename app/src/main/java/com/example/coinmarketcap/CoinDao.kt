package com.example.coinmarketcap

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCoin(coin: Coin)

    @Query("SELECT * FROM coin")
    fun loadAll(): List<Coin>
}