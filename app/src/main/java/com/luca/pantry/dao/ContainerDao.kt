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

    @Query("SELECT * FROM containers")
    suspend fun getAllContainers(): List<Container>

    @Delete
    suspend fun deleteAll(containers: List<Container>)

    @Query("DELETE FROM containers WHERE nameContainer = :nameContainer")
    suspend fun deleteContainer(nameContainer: String)

    @Query("UPDATE containers SET nameContainer = :newName WHERE nameContainer = :oldName")
    suspend fun updateNameContainer(oldName: String, newName: String)

    @Query("UPDATE containers SET imageuri = :imageName WHERE nameContainer = :nameContainer")
    suspend fun updateImageContainer(nameContainer: String, imageName: String)

}