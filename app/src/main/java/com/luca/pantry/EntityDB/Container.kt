package com.luca.pantry.EntityDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers")
data class Container(
    val nameContainer: String,
    val imageuri: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
