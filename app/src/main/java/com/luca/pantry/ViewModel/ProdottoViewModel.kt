package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import com.luca.pantry.Database.CurrentHousehold
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.dao.ProdottoDao

class ProdottoViewModel (private val dao: ProdottoDao): ViewModel() {
    suspend fun getExpiringItems(): List<Prodotto> {
        return dao.getExpiringItems(CurrentHousehold.id())
    }

    suspend fun getAllItem(): List<Prodotto> {
        return dao.getAllItems(CurrentHousehold.id())
    }

    suspend fun getItemsByContainer(containerId: String): List<Prodotto> {
        return dao.getItemsByContainer(containerId)
    }


    suspend fun updateQuantity(prodotto: Prodotto, newQuantity: Int) {
        dao.update(
            prodotto.copy(
                quantity = newQuantity,
                updatedAt = System.currentTimeMillis(),
                syncState = 1
            )
        )
    }

    suspend fun delete(prodotto: Prodotto) {
        dao.softDelete(prodotto.id, System.currentTimeMillis())
    }
}