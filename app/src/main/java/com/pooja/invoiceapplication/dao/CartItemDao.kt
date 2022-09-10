package com.pooja.invoiceapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pooja.invoiceapplication.util.CartItem

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item : CartItem)

    @Query("SELECT * FROM cartitem_table")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("UPDATE cartitem_table SET quantity=:quantity WHERE id=:id")
    suspend fun updateQuantity(id: Int, quantity: Int)

    @Query("UPDATE cartitem_table SET discount=:discount")
    suspend fun updateDiscount(discount: Int)

    @Query("UPDATE cartitem_table SET totalItemPrice=:totalItemPrice WHERE id=:id")
    suspend fun updatePrice(id: Int, totalItemPrice: Double)

    @Query("DELETE FROM cartitem_table")
    suspend fun deleteAllItems()

}