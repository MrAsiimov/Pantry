package com.luca.pantry.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.EntityDB.Household
import com.luca.pantry.dao.ContainerDao
import com.luca.pantry.dao.HouseholdDao
import com.luca.pantry.dao.ProdottoDao

@Database(entities = [Household::class, Prodotto::class, Container::class], version = 6, exportSchema = true)
abstract class DatabaseApp : RoomDatabase() {
    abstract fun prodottoDao(): ProdottoDao

    abstract fun containerDao(): ContainerDao

    abstract fun householdDao(): HouseholdDao
}