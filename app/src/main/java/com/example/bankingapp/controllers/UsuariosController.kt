package com.example.bankingapp.controllers

import android.util.Log
import com.example.bankingapp.data.Usuarios
import com.example.bankingapp.db.UsuariosDAO

suspend fun getAllUsuarios(usuariosDao: UsuariosDAO): List<Usuarios> {
    return try {
        usuariosDao.getAll()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

suspend fun getUsuarioById(id: Int, usuariosDao: UsuariosDAO): Usuarios? {
    return try {
        usuariosDao.getById(id)
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        null
    }
}

suspend fun insertUsuario(firstName: String, lastName: String, email: String, phone: String, password: String, usuariosDao: UsuariosDAO) : Usuarios? {
    try {
        val novoUsuario = Usuarios(firstName = firstName, lastName = lastName, email = email, phone = phone, password = password)
        usuariosDao.insert(novoUsuario)
        return novoUsuario
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
        return null
    }
}

suspend fun deleteUsuario(id: Int, usuariosDao: UsuariosDAO) {
    try {
        usuariosDao.delete(id)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "${e.message}")
    }
}

suspend fun updateUsuario(id: Int, firstName: String, lastName: String, email: String, phone: String, password: String, usuariosDao: UsuariosDAO) {
    try {
        usuariosDao.update(Usuarios(id = id, firstName = firstName, lastName = lastName, email = email, phone = phone, password = password))
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "${e.message}")
    }
}