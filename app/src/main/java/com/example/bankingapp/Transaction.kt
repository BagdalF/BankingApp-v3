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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankingapp.controllers.getAllUsuarios
import com.example.bankingapp.controllers.insertTransacao
import com.example.bankingapp.data.Usuarios
import com.example.bankingapp.db.TransacoesDAO
import com.example.bankingapp.db.UsuariosDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionScreen(
    currentUser: Usuarios?,
    usuariosDao: UsuariosDAO,
    transacoesDao: TransacoesDAO
) {
    var searchLastName by remember { mutableStateOf("CF0000-AC") }
    var amount by remember { mutableStateOf("0.00") }
    var currency by remember { mutableStateOf("R$") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

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
                value = searchLastName,
                onValueChange = { searchLastName = it },
                label = { Text("Search By Last Name") },
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

                    val amt = amount.toDoubleOrNull()
                    if (currentUser == null) {
                        errorMsg = "No user logged in."
                        return@Button
                    }
                    if (amt == null || amt <= 0.0) {
                        errorMsg = "Invalid amount."
                        return@Button
                    }

                    coroutineScope.launch {
                        try {
                            val usuarios = withContext(Dispatchers.IO) { getAllUsuarios(usuariosDao) }

                            // busca por primeiro nome, último nome ou "nome completo"
                            val receiver = usuarios.find {
                                it.firstName.equals(searchLastName, ignoreCase = true) ||
                                        it.lastName.equals(searchLastName, ignoreCase = true) ||
                                        "${it.firstName} ${it.lastName}".equals(searchLastName, ignoreCase = true)
                            }

                            if (receiver == null) {
                                errorMsg = "Receiver not found."
                                return@launch
                            }
                            if (receiver.id == currentUser.id) {
                                errorMsg = "Cannot transfer to yourself."
                                return@launch
                            }

                            // criar data atual
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dateStr = dateFormat.format(Date())

                            withContext(Dispatchers.IO) {
                                insertTransacao(
                                    description = "Transaction to ${receiver.firstName} ${receiver.lastName}",
                                    idSender = currentUser.id,
                                    idReceiver = receiver.id,
                                    amount = amt,
                                    date = dateStr,
                                    currency = currency,
                                    transacoesDao = transacoesDao
                                )
                            }

                            // atualizar UI no Main
                            successMessage = "Transaction successful!"
                            searchLastName = ""
                            amount = "0.00"
                        } catch (e: Exception) {
                            errorMsg = "Transaction failed: ${e.localizedMessage ?: e.message}"
                        }
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