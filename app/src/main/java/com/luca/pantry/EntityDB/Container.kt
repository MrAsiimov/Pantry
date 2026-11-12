package com.luca.pantry.EntityDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "containers",
    foreignKeys = [ForeignKey(entity = Household::class,
        parentColumns = ["id"],
        childColumns = ["household_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("household_id")]
)
data class Container(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo (name = "household_id")
    val householdId: String,
    val name: String,
    @ColumnInfo (name = "icon_key")
    val iconKey: String,
    @ColumnInfo (name = "created_at")
    val createdAt: Long,
    @ColumnInfo (name = "updated_at")
    val updatedAt: Long,
    val deleted: Boolean = false,
    @ColumnInfo (name = "sync_state")
    val syncState: Int = 1      //1 = da inviare; 0 = inviato
)
