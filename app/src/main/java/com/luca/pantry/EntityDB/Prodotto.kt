package com.luca.pantry.EntityDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(entity = Container::class,
        parentColumns = ["id"],
        childColumns = ["container_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("container_id"), Index("expiry_date"), Index("barcode")]
)
data class Prodotto(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo (name = "container_id")
    val containerId: String,
    val name: String,
    val barcode: String?,
    @ColumnInfo (name = "expiry_date")
    val expiryDate: String,
    val quantity: Int,
    @ColumnInfo (name = "image_url")
    val imageUrl: String?,
    @ColumnInfo (name = "created_at")
    val createdAt: Long,
    @ColumnInfo (name = "updated_at")
    val updatedAt: Long,
    val deleted: Boolean = false,
    @ColumnInfo (name = "sync_state")
    val syncState: Int = 1
)
