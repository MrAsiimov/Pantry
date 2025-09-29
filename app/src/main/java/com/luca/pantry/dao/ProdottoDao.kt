package com.luca.pantry.dao

import androidx.room.*
import com.luca.pantry.EntityDB.Prodotto

@Dao
interface ProdottoDao {
    @Insert
    suspend fun additem(prodotto: Prodotto)

    @Query("DELETE FROM prodotti WHERE barcode = :barcode")
    suspend fun deleteItem(barcode: String)

    @Query("SELECT * FROM prodotti WHERE quantity > 0")
    suspend fun getAllItems(): List<Prodotto>

    @Query("SELECT * FROM prodotti ORDER BY expiringDate ASC")
    suspend fun getExpiringItems(): List<Prodotto>


}