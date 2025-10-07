package com.example.bankingapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bankingapp.data.Usuarios

@Dao
interface UsuariosDAO {

    @Insert
    suspend fun insert(usuario: Usuarios)

    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<Usuarios>

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    suspend fun getById(usuarioId: Int): Usuarios

    @Query("DELETE FROM usuarios WHERE id = :usuarioId")
    suspend fun delete(usuarioId: Int)

    @Update
    suspend fun update(usuario: Usuarios)
}

