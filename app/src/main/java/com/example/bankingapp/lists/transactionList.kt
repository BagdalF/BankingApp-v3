package com.example.bankingapp.lists

import com.example.bankingapp.controllers.TransactionData

// Mutable list to store user transactions
val transactionList: MutableList<TransactionData> = mutableListOf()


// Function to add initial generic data (optional, you can also add directly)
fun populateWithGenericTransactions() {
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
    // Add more TransactionData objects as needed
}

// You can call this function from somewhere appropriate in your app,
// for example, when the application starts or when you need to initialize this data.
// Or, if this list is meant to be globally accessible with initial data,
// you can populate it directly in an init block or by calling the function here.

// Example of populating it right when this file is loaded (if appropriate for your app structure)
