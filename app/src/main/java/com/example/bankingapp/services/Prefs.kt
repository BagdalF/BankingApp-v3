package com.example.bankingapp.services

import android.content.Context
import android.content.SharedPreferences
import com.example.bankingapp.AppDatabase
import com.example.bankingapp.controllers.getUsuarioById
import com.example.bankingapp.data.Usuarios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val usuariosDao = AppDatabase.getDatabase(context).usuariosDAO()

    /**
     * Recupera o usuário logado do banco de dados usando o ID armazenado no SharedPreferences.
     * Essa operação é síncrona (usa runBlocking), pois Preferences geralmente é acessado em Composables.
     */
    var user: Usuarios?
        get() {
            val id = sharedPreferences.getInt(KEY_USER_ID, -1)
            if (id == -1) return null
            return runBlocking(Dispatchers.IO) {
                getUsuarioById(id, usuariosDao)
            }
        }
        set(value) {
            if (value != null) {
                editor.putInt(KEY_USER_ID, value.id)
                editor.apply()
            } else {
                removeUser()
            }
        }

    var isLogged: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED, false)
        set(value) {
            editor.putBoolean(KEY_IS_LOGGED, value)
            editor.apply()
        }

    fun removeUser() {
        editor.remove(KEY_USER_ID).apply()
    }

    fun clearAll() {
        editor.clear().apply()
    }

    companion object {
        private const val PREF_NAME = "BankingAppPreferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED = "is_logged"
    }
}