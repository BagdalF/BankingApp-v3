package com.example.bankingapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankingapp.controllers.ProfileData
import com.example.bankingapp.controllers.TransactionData
import com.example.bankingapp.lists.profileList
import com.example.bankingapp.lists.transactionList

@Composable
fun TransactionScreen(currentUser: ProfileData?) {

    var accolade by remember { mutableStateOf("CF0000-AC") }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("0.00") }
    var currency by remember { mutableStateOf("R$") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = accolade,
                onValueChange = { accolade = it },
                label = { Text("ACCD / Account number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    errorMsg = null
                    successMessage = null
                    val receiver = profileList.find { it.firstName.equals(name, true) || it.lastName.equals(name, true) }
                    val amt = amount.toDoubleOrNull()
                    if (currentUser == null) {
                        errorMsg = "No user logged in."
                    } else if (receiver == null) {
                        errorMsg = "Receiver not found."
                    } else if (amt == null || amt <= 0.0) {
                        errorMsg = "Invalid amount."
                    } else if (receiver.id == currentUser.id) {
                        errorMsg = "Cannot transaction to yourself."
                    } else {
                        // Add transaction to transactionList
                        val newId = (transactionList.maxOfOrNull { it.id } ?: 0) + 1
                        transactionList.add(
                            TransactionData(
                                id = newId,
                                date = "2025",
                                description = "Transaction to ${receiver.firstName} ${receiver.lastName}",
                                idReceiver = receiver.id,
                                idSender = currentUser.id,
                                amount = amt,
                                currency = currency
                            )
                        )
                        accolade = "CF0000-AC"
                        name = ""
                        amount = "0.00"
                        successMessage = "Transaction successful!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text("Send", color = Color.White, fontSize = 16.sp)
            }

            errorMsg?.let {
                Text(it, color = Color.Red, fontSize = 14.sp)
            }
            successMessage?.let {
                Text(it, color = Color.Green, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TransactionPreview() {
//    TransactionScreen()
//}
