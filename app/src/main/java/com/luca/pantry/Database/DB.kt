package com.luca.pantry.Database

import com.luca.pantry.PantryApp
import com.luca.pantry.dao.ProdottoDao

object DB {
    val prodottoDao: ProdottoDao get() = PantryApp.database.prodottoDao();
}