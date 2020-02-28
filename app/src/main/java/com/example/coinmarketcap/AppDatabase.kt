package com.example.coinmarketcap

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

 @Database(entities = [Coin::class], version = 1)
 abstract class AppDatabase : RoomDatabase() {
     abstract fun coinDao(): CoinDao

     companion object {
         fun getInstance(context: Context): AppDatabase {
             val db = Room.databaseBuilder(
                 context.applicationContext,
                 AppDatabase::class.java, "coin"
             ).build()
             return db
         }
     }
}