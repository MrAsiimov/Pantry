package com.luca.pantry.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.luca.pantry.EntityDB.Container

@Dao
interface ContainerDao {
    @Insert
    suspend fun addContainer(container: Container)

    @Query("SELECT * FROM containers WHERE household_id = :householdId AND deleted = 0 ORDER BY name")
    suspend fun getContainers(householdId: String): List<Container>

    @Query("UPDATE containers SET name = :newName, updated_at = :now, sync_state = 1 WHERE id = :id")
    suspend fun rename(id: String, newName: String, now: Long)

    @Query("UPDATE containers SET icon_key = :iconKey, updated_at = :now, sync_state = 1 WHERE id = :id")
    suspend fun updateIcon(id: String, iconKey: String, now: Long)

    @Query("UPDATE containers SET deleted = 1, updated_at = :now, sync_state = 1 WHERE id = :id")
    suspend fun softDelete(id: String, now: Long)

    @Query("""
        SELECT COUNT(*) FROM containers
        WHERE household_id = :householdId
            AND deleted = 0
            AND name = :name COLLATE NOCASE
            AND id != :excludeId
    """)
    suspend fun countByName(householdId: String, name: String, excludeId: String): Int

    @Query("SELECT COUNT(*) FROM containers WHERE household_id = :householdId AND deleted = 0")
    suspend fun conutContainers(householdId: String): Int

}