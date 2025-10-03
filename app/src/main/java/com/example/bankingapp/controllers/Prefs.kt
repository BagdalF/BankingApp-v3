package com.example.bankingapp.controllers

import android.content.Context
import android.content.SharedPreferences
import com.example.bankingapp.lists.profileList

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    var user: ProfileData?
        get() {
            val id = sharedPreferences.getInt(KEY_USER_ID, -1)
            return if (id == -1) {
                null
            } else {
                profileList.find { it.id == id }
            }
        }
        set(value) {
            if (value != null) {
                editor.putInt(KEY_USER_ID, value.id)
                editor.apply()
            }
        }

    var isLogged: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED, false)
        set(value) {
            editor.putBoolean(KEY_IS_LOGGED, value)
            editor.apply()
        }

    fun removeUser() {
        editor.remove(KEY_USER_ID)
            .apply()
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

// Data class for user profile
data class ProfileData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String
)

data class TransactionData(
    val id: Int,
    val date: String,
    val description: String?,
    val idReceiver: Int,
    val idSender: Int,
    val amount: Double,
    val currency: String
)