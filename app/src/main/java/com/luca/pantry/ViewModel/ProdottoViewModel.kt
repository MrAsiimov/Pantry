package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.dao.ProdottoDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ProdottoViewModel (private val dao: ProdottoDao): ViewModel() {
    private val _toastMessage = MutableSharedFlow<String>(replay = 1)
    val toastMessage: SharedFlow<String> = _toastMessage

    suspend fun getExpiringItems(): List<Prodotto> {
        _toastMessage.emit("Lista aggiornata da viewmodel")
        return dao.getExpiringItems()
    }

    suspend fun getAllItem(): List<Prodotto> {
        _toastMessage.emit("Lista aggiornata da viewmodel")
        return dao.getAllItems()
    }

    suspend fun getItemsByContainer(container: String): List<Prodotto> {
        _toastMessage.emit("Lista aggiornata da viewmodel")
        return dao.getItemsByContainer(container)
    }


    suspend fun updateQuantity(prodotto: Prodotto, newQuantity: Int) {
        _toastMessage.emit("Prodotto aggiornato da viewmodel")
        dao.update(prodotto.copy(quantity = newQuantity))
    }

    suspend fun delete(prodotto: Prodotto) {
        _toastMessage.emit("Prodotto eliminato da viewmodel")
        dao.deleteProduct(prodotto)
    }
}