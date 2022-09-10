package com.pooja.invoiceapplication.view

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pooja.invoiceapplication.R
import com.pooja.invoiceapplication.databinding.ActivityCartBinding
import com.pooja.invoiceapplication.databinding.CustomDialogBinding
import com.pooja.invoiceapplication.util.CartItem
import com.pooja.invoiceapplication.util.adapter.CartAdapter
import com.pooja.invoiceapplication.util.adapter.CartClickedListeners
import com.pooja.invoiceapplication.util.adapter.InvoiceAdapter
import com.pooja.invoiceapplication.viewmodel.CartViewModel

class CartActivity : AppCompatActivity(),CartClickedListeners {
    lateinit var binding:ActivityCartBinding
    lateinit var viewModel:CartViewModel
    lateinit var cartAdapter:CartAdapter
    lateinit var cartItemList:List<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initView()
        viewModel.getAllCartItems().observe(this, Observer<List<CartItem>>{
            cartAdapter.setCartItem(it)
            cartItemList=it
        })
        binding.cartActivityCheckoutBtn.setOnClickListener {
            //to display dialog
            openBottomsheetDialog()
        }
    }

    fun initView(){
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(CartViewModel::class.java)
        cartAdapter=CartAdapter(this)
        binding.cartRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    override fun onDeleteClicked(cartItem: CartItem) {
        viewModel.deleteCartItem(cartItem)
    }

    override fun onPlusClicked(cartItem: CartItem) {
        val quantity: Int = cartItem.quantity!! + 1
        viewModel.updateQuantity(cartItem.id!!, quantity)
        viewModel.updatePrice(cartItem.id!!, quantity * cartItem.price!!.toDouble())
        cartAdapter.notifyDataSetChanged()
    }

    override fun onMinusClicked(cartItem: CartItem) {
        val quantity: Int = cartItem.quantity!! - 1
        if(quantity != 0){
            viewModel.updateQuantity(cartItem.id!!,quantity)
            viewModel.updatePrice(cartItem.id!!,quantity*cartItem.price!!.toDouble())
            cartAdapter.notifyDataSetChanged()
        }else{
            viewModel.deleteCartItem(cartItem)
        }
    }


    fun openBottomsheetDialog(){
        var dialog=BottomSheetDialog(this)
        var bindingCustom=CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingCustom.root)
        var discount:Int=0
        bindingCustom.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val radio:RadioButton?=findViewById(i)
            if(bindingCustom.flat.isChecked){
                bindingCustom.percentageoptionEt.visibility=View.INVISIBLE
                bindingCustom.flatoptionTv.visibility=View.VISIBLE
                bindingCustom.customlinearLayout.visibility=View.VISIBLE
                discount=10
            }else if(bindingCustom.percentage.isChecked){
                bindingCustom.flatoptionTv.visibility=View.GONE
                bindingCustom.percentageoptionEt.visibility=View.VISIBLE
                bindingCustom.customlinearLayout.visibility=View.VISIBLE

            }
        })
        bindingCustom.customYesBtn.setOnClickListener {
           if(bindingCustom.percentageoptionEt.isVisible) {
               if(bindingCustom.percentageoptionEt.text.toString().isNotBlank()) {
                   discount = bindingCustom.percentageoptionEt.text.toString().toInt()
                }
               else
                   Toast.makeText(applicationContext,"Enter discount value ",Toast.LENGTH_LONG).show()
           }
            discountCalcutale(cartItemList,discount)
            var intent=Intent(applicationContext,InvoiceActivity::class.java)
            startActivity(intent)
            dialog.cancel()
        }

        bindingCustom.customNoBtn.setOnClickListener {
               dialog.cancel()
        }
        dialog.show()
    }

    fun discountCalcutale(cartItemList:List<CartItem>,discount:Int){
        if(discount != 0){
            viewModel.updateDiscount(discount)
        }
    }
}