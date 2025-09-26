package com.luca.pantry.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.dao.ContainerDao
import com.luca.pantry.dao.ProdottoDao

@Database(entities = [Prodotto::class, Container::class], version = 5)
abstract class DatabaseApp : RoomDatabase() {
    abstract fun prodottoDao(): ProdottoDao

    abstract fun containerDao(): ContainerDao
}