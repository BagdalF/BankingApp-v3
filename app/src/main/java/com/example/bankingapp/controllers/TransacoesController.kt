package com.example.bankingapp.controllers

import android.util.Log
import com.example.bankingapp.data.Transacoes
import com.example.bankingapp.db.TransacoesDAO

suspend fun getAllTransacoes(transacoesDao: TransacoesDAO): List<Transacoes> {
    return try {
        transacoesDao.getAll()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

suspend fun getTransacaoById(id: Int, transacoesDao: TransacoesDAO): Transacoes? {
    return try {
        transacoesDao.getById(id)
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        null
    }
}

suspend fun insertTransacao(description: String, idSender: Int, idReceiver: Int, amount: Double, date: String, currency: String, transacoesDao: TransacoesDAO) {
    try {
        transacoesDao.insert(Transacoes(description = description, idSender = idSender, idReceiver = idReceiver, amount = amount, date = date, currency = currency))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun deleteTransacao(id: Int, transacoesDao: TransacoesDAO) {
    try {
        transacoesDao.delete(id)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "${e.message}")
    }
}

suspend fun updateTransacao(id: Int, description: String, idSender: Int, idReceiver: Int, amount: Double, date: String, currency: String, transacoesDao: TransacoesDAO) {
    try {
        transacoesDao.update(Transacoes(id = id, description = description, idSender = idSender, idReceiver = idReceiver, amount = amount, date = date, currency = currency))
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "${e.message}")
    }
}