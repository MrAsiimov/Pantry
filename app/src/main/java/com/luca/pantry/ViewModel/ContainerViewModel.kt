package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.dao.ContainerDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ContainerViewModel (private val dao: ContainerDao): ViewModel() {
    private val _toastMessage = MutableSharedFlow<String>(replay = 1)
    val toastMessage: SharedFlow<String> = _toastMessage

    suspend fun getAllContainers(): List<Container> {
        _toastMessage.emit("Lista aggiornata da viewmodel")
        return dao.getAllContainers()
    }

    suspend fun addContainer(container: Container) {
        _toastMessage.emit("Container aggiunto da viewmodel")
        dao.addContainer(container)
    }

    suspend fun deleteContainer(container: Container) {
        _toastMessage.emit("Container eliminato da viewmodel")
        dao.deleteContainer(container.nameContainer)
    }

    suspend fun updateNameContainer(oldName: String, newName: String) {
        _toastMessage.emit("Nome container aggiornato da viewmodel")
        dao.updateNameContainer(oldName, newName)
    }
}