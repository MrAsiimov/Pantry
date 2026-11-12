package com.luca.pantry.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luca.pantry.EntityDB.Household

@Dao
interface HouseholdDao {
    @Insert
    suspend fun insert (household: Household)

    @Query ("SELECT COUNT(*) FROM households WHERE deleted = 0")
    suspend fun count(): Int

    //Provvisorio, finchè non c'è il selettore di household
    @Query ("SELECT * FROM households WHERE deleted = 0 ORDER BY created_at LIMIT 1")
    suspend fun getDefault(): Household?
}