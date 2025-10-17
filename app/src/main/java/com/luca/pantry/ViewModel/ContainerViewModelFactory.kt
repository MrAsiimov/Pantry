package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luca.pantry.dao.ContainerDao

class ContainerViewModelFactory(private val dao: ContainerDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContainerViewModel(dao) as T
    }
}