package com.pooja.invoiceapplication.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.pooja.invoiceapplication.dao.CartItemDao
import com.pooja.invoiceapplication.util.CartItem

class CartRepo(var cartItemDao: CartItemDao) {
    fun getAllCartItems():LiveData<List<CartItem>> = cartItemDao.getAllCartItems()

    suspend fun insertItem(item : CartItem)=
            cartItemDao.insertItem(item)

    suspend fun deleteCartItem(cartItem: CartItem)=
            cartItemDao.deleteCartItem(cartItem)

    suspend fun updateQuantity(id: Int, quantity: Int)=
            cartItemDao.updateQuantity(id,quantity)

    suspend fun updateDiscount(discount: Int)=
            cartItemDao.updateDiscount(discount)
    suspend fun updatePrice(id: Int, totalItemPrice: Double)=cartItemDao.updatePrice(id,totalItemPrice)

    suspend fun deleteAllItems()=cartItemDao.deleteAllItems()

}