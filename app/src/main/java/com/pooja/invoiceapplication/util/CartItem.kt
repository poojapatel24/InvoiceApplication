package com.pooja.invoiceapplication.util

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartitem_table")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "price")
    var price: Long?,

    @ColumnInfo(name = "quantity")
    var quantity: Int?,

    @ColumnInfo(name = "totalItemPrice")
    var totalItemPrice: Double?,

    @ColumnInfo(name="discount")
    var discount:Int


)
