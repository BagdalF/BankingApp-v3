package com.example.bankingapp.controllers

import android.util.Log
import com.example.bankingapp.data.Filmes
import com.example.bankingapp.db.FilmesDAO

suspend fun getAllFilmes(filmesDao: FilmesDAO): List<Filmes> {
    return try {
        filmesDao.getAll()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

suspend fun getFilmeById(id: Int, filmesDao: FilmesDAO): Filmes? {
    return try {
        filmesDao.getById(id)
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        return null
    }
}

suspend fun insertFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
    try{
        filmesDao.insert(Filmes(nome=nome, desc = desc))
    }catch (e: Exception){
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun deleteFilme(id: Int, filmesDao: FilmesDAO) {
    try {
        filmesDao.delete(id)
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
    }
}

suspend fun updateFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
    try {
        filmesDao.update(Filmes(nome= nome, desc= desc))
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
    }
}