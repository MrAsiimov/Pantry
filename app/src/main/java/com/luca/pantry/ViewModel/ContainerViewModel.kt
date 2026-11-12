package com.luca.pantry.ViewModel

import androidx.lifecycle.ViewModel
import com.luca.pantry.Database.CurrentHousehold
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.dao.ContainerDao

class ContainerViewModel (private val dao: ContainerDao): ViewModel() {
    suspend fun getAllContainers(): List<Container> {
        return dao.getContainers(CurrentHousehold.id())
    }

    suspend fun addContainer(container: Container) {
        dao.addContainer(container)
    }

    suspend fun renameContainer(container: Container, newName: String) {
        dao.rename(container.id, newName, System.currentTimeMillis())
    }

    suspend fun updateIcon(container: Container, iconKey: String) {
        dao.updateIcon(container.id, iconKey, System.currentTimeMillis())
    }

    suspend fun isNameTaken(name: String, excludeId: String = ""): Boolean = dao.countByName(
        CurrentHousehold.id(), name, excludeId) > 0
}