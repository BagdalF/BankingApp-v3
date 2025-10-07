package com.example.bankingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacoes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val idSender: Int,
    val idReceiver: Int,
    val amount: Double,
    val date: String,
    val currency: String
)