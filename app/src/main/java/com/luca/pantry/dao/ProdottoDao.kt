package com.luca.pantry.dao

import androidx.room.*
import com.luca.pantry.EntityDB.Prodotto

@Dao
interface ProdottoDao {
    @Insert
    suspend fun addItem(prodotto: Prodotto)

    @Update
    suspend fun update(prodotto: Prodotto)

    @Query("UPDATE products SET deleted = 1, updated_at = :now, sync_state = 1 WHERE id = :id")
    suspend fun softDelete(id: String, now: Long)

    @Query("UPDATE products SET deleted = 1, updated_at = :now, sync_state = 1 WHERE container_id = :containerId")
    suspend fun softDeleteByContainer(containerId: String, now: Long)

    @Query("""
        SELECT p.* FROM products p
        JOIN containers c ON c.id = p.container_id
        WHERE c.household_id = :householdId
            AND p.deleted = 0 AND c.deleted = 0 AND p.quantity > 0
    """)
    suspend fun getAllItems(householdId: String): List<Prodotto>

    @Query("""
        SELECT p.* FROM products p
        JOIN containers c ON c.id = p.container_id
        WHERE c.household_id = :householdId
            AND p.deleted = 0 AND c.deleted = 0 AND p.quantity > 0
        ORDER BY p.expiry_date ASC
    """)
    suspend fun getExpiringItems(householdId: String): List<Prodotto>

    @Query("SELECT * FROM products WHERE container_id = :containerId AND deleted = 0")
    suspend fun getItemsByContainer(containerId: String): List<Prodotto>

    //For the notifications worker, so that all the user's households are grouped togheter
    @Query("""
        SELECT p.* FROM products p
        JOIN containers c ON c.id = p.container_id
        WHERE p.deleted = 0 AND c.deleted = 0 AND p.quantity > 0
        ORDER BY p.expiry_date ASC
    """)
    suspend fun getAllActiveItems(): List<Prodotto>

    @Query("SELECT * FROM products WHERE deleted = 0 AND barcode IS NOT NULL AND (image_url IS NULL OR image_url = '')")
    suspend fun getItemsMissingImage(): List<Prodotto>

    @Query("""
        UPDATE products
        SET image_url = :imageUrl, updated_at = :now, sync_state = 1
        WHERE id = :id AND barcode = :barcode AND (image_url IS NULL OR image_url = '')
    """)
    suspend fun setImageIfMissing(id: String, barcode: String, imageUrl: String, now: Long)
}