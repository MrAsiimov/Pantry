package com.luca.pantry.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.dao.ProdottoDao

@Database(entities = [Prodotto::class], version = 1)
abstract class DatabaseApp : RoomDatabase() {
    abstract fun prodottoDao(): ProdottoDao
}