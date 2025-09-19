package com.luca.pantry

import android.app.Application
import androidx.room.Room
import com.luca.pantry.Database.DatabaseApp
import com.luca.pantry.dao.ContainerDao

class PantryApp: Application() {
    companion object {
        lateinit var database: DatabaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            DatabaseApp::class.java,
            "pantry_database"
        ).build()
    }

    val containerDao: ContainerDao
        get() = database.containerDao()
}