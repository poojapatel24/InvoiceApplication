package com.pooja.invoiceapplication.util.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pooja.invoiceapplication.databinding.InvoiceItemBinding
import com.pooja.invoiceapplication.util.CartItem

class InvoiceAdapter:RecyclerView.Adapter<InvoiceViewHoler>() {
      var allcartItemList:List<CartItem>?= null

    fun setInvoiceItem(cartItemList: List<CartItem>){
        allcartItemList=cartItemList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHoler {
       var view=LayoutInflater.from(parent.context)
       var binding=InvoiceItemBinding.inflate(view,parent,false)
       return InvoiceViewHoler(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHoler, position: Int) {
        var cartItem= allcartItemList?.get(position)
        holder.binding.invoiceItem.text=cartItem?.name
        holder.binding.invoicequantity.text="Quantity X "+cartItem?.quantity.toString()
        holder.binding.invoicePrice.text=cartItem?.price.toString()
    }

    override fun getItemCount(): Int {
        if(allcartItemList == null){
            return 0
        }else{
            return allcartItemList?.size!!
        }
    }
}
class InvoiceViewHoler(var binding: InvoiceItemBinding):RecyclerView.ViewHolder(binding.root){

}