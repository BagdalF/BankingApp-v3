package com.example.bankingapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bankingapp.data.Filmes

@Dao
interface FilmesDAO{

    @Insert
    suspend fun insert(filme: Filmes)

    @Query("SELECT * FROM filmes")
    suspend fun getAll() : List<Filmes>

    @Query("SELECT * FROM filmes WHERE id = :filmeId")
    suspend fun getById(filmeId : Int) : Filmes

    @Query("DELETE FROM filmes WHERE id = :filmeId")
    suspend fun delete(filmeId: Int)

    @Update
    suspend fun update(filme: Filmes)
}

