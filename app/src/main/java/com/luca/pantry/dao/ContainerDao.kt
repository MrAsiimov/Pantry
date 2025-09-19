package com.luca.pantry.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luca.pantry.EntityDB.Container

@Dao
interface ContainerDao {
    @Insert
    suspend fun addContainer(container: Container)

    @Query("SELECT * FROM containers")
    suspend fun getAllContainers(): List<Container>
}