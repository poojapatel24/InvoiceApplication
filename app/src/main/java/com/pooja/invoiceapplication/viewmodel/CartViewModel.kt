package com.pooja.invoiceapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pooja.invoiceapplication.database.CartDataBase
import com.pooja.invoiceapplication.repo.CartRepo
import com.pooja.invoiceapplication.util.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application): AndroidViewModel(application){
    val allCartItem:LiveData<List<CartItem>>
    var cartRepo:CartRepo
    init {
        val dao=CartDataBase.getDatabase(application).getCartItemDao()
        cartRepo= CartRepo(dao)
        allCartItem=cartRepo.getAllCartItems()
    }
    fun getAllCartItems():LiveData<List<CartItem>> = cartRepo.getAllCartItems()
    fun insertItem(item : CartItem)=viewModelScope.launch(Dispatchers.IO){cartRepo.insertItem(item)}
    fun updateQuantity(id: Int, quantity: Int)=
        viewModelScope.launch(Dispatchers.IO){cartRepo.updateQuantity(id,quantity)}
    fun deleteCartItem(cartItem: CartItem)=
        viewModelScope.launch(Dispatchers.IO){cartRepo.deleteCartItem(cartItem)}
    fun updatePrice(id: Int, totalItemPrice: Double)=
        viewModelScope.launch(Dispatchers.IO){cartRepo.updatePrice(id,totalItemPrice)}
    fun updateDiscount(discount: Int)=
        viewModelScope.launch(Dispatchers.IO){cartRepo.updateDiscount(discount)}
    fun deleteAllItems()=
        viewModelScope.launch(Dispatchers.IO){cartRepo.deleteAllItems()}

}