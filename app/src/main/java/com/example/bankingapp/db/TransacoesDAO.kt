package com.example.bankingapp.db

import androidx.room.*
import com.example.bankingapp.data.Transacoes

@Dao
interface TransacoesDAO {
    @Insert
    suspend fun insert(filme: Transacoes)

    @Query("SELECT * FROM transacoes")
    suspend fun getAll(): List<Transacoes>

    @Query("SELECT * FROM transacoes WHERE id = :filmeId")
    suspend fun getById(filmeId: Int): Transacoes

    @Query("DELETE FROM transacoes WHERE id = :filmeId")
    suspend fun delete(filmeId: Int)

    @Update
    suspend fun update(filme: Transacoes)
}

