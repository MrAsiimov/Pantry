package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luca.pantry.dao.ProdottoDao

class ProdottoViewModelFactory(private val dao: ProdottoDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProdottoViewModel(dao) as T
    }
}