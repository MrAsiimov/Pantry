package com.luca.pantry.EntityDB

import androidx.room.Entity

@Entity(tableName = "prodotti", primaryKeys = arrayOf("productName, container"))
data class Prodotto(
    val productName: String,
    val expiringDate: String,
    val container: String,
    val quantity: Int,
    val barcode: String
)
