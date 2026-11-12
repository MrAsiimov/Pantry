package com.luca.pantry.EntityDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "households")
data class Household (
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ColumnInfo (name = "created_at")
    val createdAt: Long,
    @ColumnInfo (name = "updated_at")
    val updatedAt: Long,
    val deleted: Boolean = false,
    @ColumnInfo (name = "last_synced_at")
    val lastSyncedAt: Long = 0
)