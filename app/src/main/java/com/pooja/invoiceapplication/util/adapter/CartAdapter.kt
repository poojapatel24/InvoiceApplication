package com.pooja.invoiceapplication.util.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pooja.invoiceapplication.databinding.CartItemListBinding
import com.pooja.invoiceapplication.util.CartItem

class CartAdapter(var cartClickedListeners: CartClickedListeners):RecyclerView.Adapter<CartViewHolder>() {
    lateinit var cartItemList:List<CartItem>
    fun setCartItem(cartItemList: List<CartItem>){
        this.cartItemList=cartItemList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        var view=LayoutInflater.from(parent.context)
        var binding=CartItemListBinding.inflate(view,parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var cartItem=cartItemList.get(position)
        holder.setIsRecyclable(false)
        holder.binding.cartItemName.text=cartItem.name
        holder.binding.cartItemDescrTv.text=cartItem.description
        holder.binding.cartItemPriceTv.text=cartItem.price.toString()
        holder.binding.cartItemQuantityTV.text=cartItem.quantity.toString()
        holder.binding.cartItemDeleteBtn.setOnClickListener {
            cartClickedListeners.onDeleteClicked(cartItem)
        }
        holder.binding.cartItemAddQuantityBtn.setOnClickListener {
            cartClickedListeners.onPlusClicked(cartItem)
        }
        holder.binding.cartItemMinusQuantityBtn.setOnClickListener {
            cartClickedListeners.onMinusClicked(cartItem)
        }
    }

    override fun getItemCount(): Int {
        if(cartItemList == null){
            return 0
        }else{
            return cartItemList.size
        }
    }
}
class CartViewHolder(var binding: CartItemListBinding):RecyclerView.ViewHolder(binding.root){
}

interface CartClickedListeners{
    fun onDeleteClicked(cartItem: CartItem)
    fun onPlusClicked(cartItem: CartItem)
    fun onMinusClicked(cartItem: CartItem)
}