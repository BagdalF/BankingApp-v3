package com.example.bankingapp.db

import androidx.room.*
import com.example.bankingapp.data.Transacoes

@Dao
interface TransacoesDAO {
    @Insert
    suspend fun insert(transacao: Transacoes)

    @Query("SELECT * FROM transacoes")
    suspend fun getAll(): List<Transacoes>

    @Query("SELECT * FROM transacoes WHERE id = :transacaoId")
    suspend fun getById(transacaoId: Int): Transacoes

    @Query("DELETE FROM transacoes WHERE id = :transacaoId")
    suspend fun delete(transacaoId: Int)

    @Update
    suspend fun update(transacao: Transacoes)
}

