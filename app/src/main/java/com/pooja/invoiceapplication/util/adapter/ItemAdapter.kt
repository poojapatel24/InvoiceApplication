package com.pooja.invoiceapplication.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pooja.invoiceapplication.R
import com.pooja.invoiceapplication.databinding.ItemListBinding
import com.pooja.invoiceapplication.util.ItemData

class ItemAdapter(var itemClickedListeners:ItemClickedListeners):RecyclerView.Adapter<ItemViewHolder>(){
    var itemDataList=ArrayList<ItemData>()
    fun setItemList(itemDataList:ArrayList<ItemData>){
       this.itemDataList=itemDataList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view=LayoutInflater.from(parent.context)
        val binding=ItemListBinding.inflate(view,parent,false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var itemData=itemDataList.get(position)
        holder.binding.eachItemName.text=itemData.name
        holder.binding.eachItemDesTv.text=itemData.description
        holder.binding.eachitemPriceTv.text=itemData.price.toString()
        holder.binding.eachItemAddToCartBtn.setOnClickListener {
            itemClickedListeners.addToCartClicked(itemData)
        }
    }

    override fun getItemCount(): Int {
        return itemDataList.size
    }
}
class ItemViewHolder(var binding: ItemListBinding):RecyclerView.ViewHolder(binding.root){
}
interface ItemClickedListeners{
    fun addToCartClicked(itemData: ItemData)
}