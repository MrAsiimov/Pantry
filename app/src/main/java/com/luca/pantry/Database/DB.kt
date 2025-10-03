package com.luca.pantry.Database

import android.content.Context
import androidx.room.Room
import com.luca.pantry.dao.ProdottoDao

object DB {
    lateinit var prodottoDao: ProdottoDao

    fun init(context: Context) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            DatabaseApp::class.java,
            "pantry_database"
        ).build()
        prodottoDao = db.prodottoDao()
    }
}