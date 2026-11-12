package com.luca.pantry.Database

import com.luca.pantry.EntityDB.Household
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.PantryApp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object CurrentHousehold {
    private val mutex = Mutex()

    suspend fun id(): String = mutex.withLock {
        val dao = PantryApp.database.householdDao()
        dao.getDefault()?.let { return@withLock it.id }

        val now = System.currentTimeMillis()
        val household = Household(name = "Casa", createdAt = now, updatedAt = now)
        dao.insert(household)
        household.id
    }

    suspend fun containerNames(): Map<String, String> =
        PantryApp.database.containerDao().getContainers(id()).associate { it.id to it.name }
}