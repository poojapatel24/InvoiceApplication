package com.pooja.invoiceapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pooja.invoiceapplication.dao.CartItemDao
import com.pooja.invoiceapplication.util.CartItem

@Database(entities = [CartItem::class], version = 1, exportSchema = false)
abstract class CartDataBase:RoomDatabase(){
    abstract fun getCartItemDao():CartItemDao
    companion object{
        @Volatile
        private var INSTANCE:CartDataBase?= null
        private const val DB_NAME="cart_database.db"

        fun getDatabase(context: Context): CartDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}