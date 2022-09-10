package com.pooja.invoiceapplication.view

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter

import com.pooja.invoiceapplication.R
import com.pooja.invoiceapplication.databinding.ActivityInvoiceBinding
import com.pooja.invoiceapplication.util.CartItem
import com.pooja.invoiceapplication.util.PdfService
import com.pooja.invoiceapplication.util.adapter.InvoiceAdapter
import com.pooja.invoiceapplication.viewmodel.CartViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.logging.FileHandler

class InvoiceActivity : AppCompatActivity() {
    lateinit var binding:ActivityInvoiceBinding
    lateinit var viewModel:CartViewModel
    lateinit var invoiceAdapter:InvoiceAdapter

    var PERMISSION_CODE = 101
    lateinit var cartitemList:List<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        initView()


        viewModel.allCartItem.observe(this,
        Observer {

            var price = 0.0
            invoiceAdapter.setInvoiceItem(it)
            for (i in it.indices) {
                price = price + it.get(i).totalItemPrice!!
            }
            binding.invoicesubTotalTv.text=price.toString()
            val discount=it.get(0).discount
            val totalDiscountAmt=((price*discount)/100)
            binding.invoiceDiscountTv.text=totalDiscountAmt.toString()
            binding.invoiceTotalPriceTv.text=(price-totalDiscountAmt).toString()
            cartitemList=it
        })
        binding.invoiceDownloadbtn.setOnClickListener {
            // on below line we are checking permission
            if (checkPermissions()) {
                // if permission is granted we are displaying a toast message.
                Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                createPdf()
            } else {
                // if the permission is not granted
                // we are calling request permission method.
                requestPermission()
            }
        }
    }
    fun initView(){
        binding=ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(CartViewModel::class.java)
        invoiceAdapter= InvoiceAdapter()
        binding.invoiceRecyclerView.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@InvoiceActivity)
            adapter=invoiceAdapter
        }



    }

    fun checkPermissions():Boolean{
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    // if permissions are granted we are displaying a toast message.
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
                createPdf()
            }
        }
    }




    private fun createPdf() {
        val onError: (Exception) -> Unit = { toastErrorMessage(it.message.toString()) }
        val onFinish: (File) -> Unit = { Toast.makeText(applicationContext,"PDF file Save",Toast.LENGTH_LONG).show() }
        val paragraphList = listOf(
            getString(R.string.invoice), getString(R.string.invoice_no_89685)
        )
        val pdfService = PdfService()
        pdfService.createUserTable(
            data = cartitemList,
            paragraphList = paragraphList,
            onFinish = onFinish,
            onError = onError
        )
    }

    private fun openFile(file: File) {
        val path = Environment.getExternalStorageDirectory().path+"/invoice.pdf"
        val pdfFile = File(path)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(pdfFile.toUri(), "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            toastErrorMessage("Can't read pdf file")
        }
    }

    private fun toastErrorMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}