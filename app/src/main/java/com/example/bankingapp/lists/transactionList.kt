package com.example.bankingapp.lists

import com.example.bankingapp.data.Transacoes
import com.example.bankingapp.db.TransacoesDAO

data class TransactionData(
    val id: Int,
    val date: String,
    val description: String?,
    val idReceiver: Int,
    val idSender: Int,
    val amount: Double,
    val currency: String
)

val transactionList: MutableList<TransactionData> = mutableListOf()

suspend fun populateWithGenericTransactions(transacoesDao: TransacoesDAO) {
    transactionList.add(
        TransactionData(
            id = 1,
            description = "Grocery Shopping",
            idSender = 1,
            idReceiver = 2,
            amount = 75.50,
            date = "2023-10-26",
            currency = "R$"
        )
    )
    transactionList.add(
        TransactionData(
            id = 2,
            description = "Utility Bill Payment",
            idSender = 1,
            idReceiver = 2,
            amount = 120.00,
            date = "2023-10-25",
            currency = "R$"
        )
    )
    transactionList.add(
        TransactionData(
            id = 3,
            description = "Online Subscription",
            idSender = 1,
            idReceiver = 2,
            amount = 15.99,
            date = "2023-10-24",
            currency = "R$"
        )
    )
    transactionList.add(
        TransactionData(
            id = 104,
            description = "Dinner with Friends",
            idSender = 1,
            idReceiver = 2,
            amount = 60.25,
            date = "2023-10-23",
            currency = "R$"
        )
    )


    for (transaction in transactionList) {
        transacoesDao.insert(
            Transacoes(
                id = transaction.id,
                description = transaction.description ?: "",
                idSender = transaction.idSender,
                idReceiver = transaction.idReceiver,
                amount = transaction.amount,
                date = transaction.date,
                currency = transaction.currency
            )
        )
    }
}
