package com.pooja.invoiceapplication.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pooja.invoiceapplication.R
import com.pooja.invoiceapplication.databinding.ActivityMainBinding
import com.pooja.invoiceapplication.util.CartItem
import com.pooja.invoiceapplication.util.ItemData
import com.pooja.invoiceapplication.util.adapter.ItemAdapter
import com.pooja.invoiceapplication.util.adapter.ItemClickedListeners
import com.pooja.invoiceapplication.viewmodel.CartViewModel
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity(),ItemClickedListeners {
    lateinit var binding:ActivityMainBinding
    var itemList=ArrayList<ItemData>()
    var cartItemList=ArrayList<CartItem>()
    var itemID = -1;
    lateinit var viewModel:CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//       viewModel= ViewModelProvider(this).get(CartViewModel::class.java)
       viewModel=ViewModelProvider(this,
           ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(CartViewModel::class.java)

        setItemDataOnList()
        binding.cartIv.setOnClickListener {
            var intent = Intent(applicationContext,CartActivity::class.java)
            startActivity(intent)
        }
        var itemAdapter= ItemAdapter(this)
        itemAdapter.setItemList(itemList)
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.adapter=itemAdapter

    }

    fun setItemDataOnList(){
        itemList.add(ItemData("Lenovo","Lenovo IdeaPad Slim 1",22000))
        itemList.add(ItemData("HP","HP 11th Gen ",32000))
        itemList.add(ItemData("ASUS","ASUS Vivobook 15",20000))
        itemList.add(ItemData("Lenovo I","Lenovo IdeaPad Slim 3",32000))
       }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCartItems().observe(this,Observer<List<CartItem>>(){
            cartItemList.addAll(it)
        })
    }

    override fun addToCartClicked(itemData: ItemData) {
       //     Toast.makeText(this,"data ${itemData.name}",Toast.LENGTH_LONG).show()
        val quantity = intArrayOf(1)
        val id = IntArray(1)
        val randomValues = List(10) { Random.nextInt(0, 100) }
        var cartItem=CartItem(randomValues[0],itemData.name, itemData.description, itemData.price,  quantity[0], itemData.price.toDouble(),0 )
        if (!cartItemList.isEmpty()) {
            for (i in cartItemList.indices) {
                if (cartItem.name.equals(cartItemList.get(i).name)) {
                    quantity[0] = cartItemList.get(i).quantity!!
                    quantity[0]++
                    id[0] = cartItemList.get(i).id!!
                }
            }
        }
        Log.d("TAG", "onAddToCartBtnClicked: " + quantity[0])
        if (quantity[0] == 1) {
            cartItem.quantity=quantity[0]
            cartItem.totalItemPrice=(quantity[0] * cartItem.price!!.toDouble())
            viewModel.insertItem(cartItem)
        } else {
            viewModel.updateQuantity(id[0], quantity[0])
            viewModel.updatePrice(id[0], quantity[0] * cartItem.price!!.toDouble())
        }
        showSnackBar("Item Added To Cart")
    }
    fun showSnackBar(msg:String){
        Snackbar.make(binding.mainCoordinatorLayout, msg, Snackbar.LENGTH_SHORT)
            .show()
    }

}