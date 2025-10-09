package com.example.bankingapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankingapp.controllers.deleteTransacao
import com.example.bankingapp.controllers.getAllTransacoes
import com.example.bankingapp.controllers.getAllUsuarios
import com.example.bankingapp.controllers.updateTransacao
import com.example.bankingapp.data.Transacoes
import com.example.bankingapp.data.Usuarios
import com.example.bankingapp.db.TransacoesDAO
import com.example.bankingapp.db.UsuariosDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun StatementScreen(
    user: Usuarios?,
    usuariosDao: UsuariosDAO,
    transacoesDao: TransacoesDAO
) {
    val coroutineScope = rememberCoroutineScope()

    var transactions by remember { mutableStateOf<List<Transacoes>>(emptyList()) }
    var usuarios by remember { mutableStateOf<List<Usuarios>>(emptyList()) }
    var balance by remember { mutableStateOf(0.0) }

    LaunchedEffect(user) {
        if (user != null) {
            coroutineScope.launch {
                val loadedTransactions = withContext(Dispatchers.IO) {
                    getAllTransacoes(transacoesDao)
                        .filter { it.idSender == user.id || it.idReceiver == user.id }
                        .sortedByDescending { it.date }
                }

                val loadedUsuarios = withContext(Dispatchers.IO) {
                    getAllUsuarios(usuariosDao)
                }

                val calculatedBalance = loadedTransactions.sumOf { trans ->
                    when (user.id) {
                        trans.idReceiver -> trans.amount
                        trans.idSender -> -trans.amount
                        else -> 0.0
                    }
                }

                transactions = loadedTransactions
                usuarios = loadedUsuarios
                balance = calculatedBalance
            }
        } else {
            transactions = emptyList()
            usuarios = emptyList()
            balance = 0.0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Card de cabeçalho com nome e saldo
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "${user?.firstName ?: ""} ${user?.lastName ?: ""}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(user?.email ?: "", fontSize = 14.sp, color = Color.Gray)
                }
                Row {
                    Text(
                        "R$ %.2f".format(balance),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    
                }
            }
        }

        // Lista de transações
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(transactions) { transaction ->
                val isSender = transaction.idSender == user?.id
                val otherId = if (isSender) transaction.idReceiver else transaction.idSender
                val otherUser = usuarios.find { it.id == otherId }
                val otherName = if (otherUser != null)
                    "${otherUser.firstName} ${otherUser.lastName}"
                else "Unknown"

                val amountPrefix = if (isSender) "- " else "+ "
                val amountColor = if (isSender) Color.Red else Color(0xFF388E3C)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(transaction.date, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(otherName, color = Color.Gray, fontSize = 14.sp)
                        Text(transaction.description, color = Color.DarkGray, fontSize = 13.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            amountPrefix + transaction.currency + " %.2f".format(transaction.amount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = amountColor
                        )
                        // Ícone de três pontinhos e menu de opções
                        var expanded by remember { mutableStateOf(false) }
                        var showDateDialog by remember { mutableStateOf(false) }
                        var newDate by remember { mutableStateOf("") }
                        androidx.compose.material3.IconButton(onClick = { expanded = true }) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.MoreVert,
                                contentDescription = "Mais opções"
                            )
                        }
                        androidx.compose.material3.DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            if (isSender) {
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Adiar Pagamento") },
                                    onClick = {
                                        expanded = false
                                        showDateDialog = true
                                    }
                                )
                            }
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text("Remover Transação") },
                                onClick = {
                                    coroutineScope.launch {
                                        deleteTransacao(
                                            id = transaction.id,
                                            transacoesDao = transacoesDao
                                        )
                                        expanded = false
                                    }
                                }
                            )
                        }
                        if (showDateDialog) {
                            androidx.compose.material3.AlertDialog(
                                onDismissRequest = { showDateDialog = false },
                                confirmButton = {
                                    androidx.compose.material3.Button(onClick = {
                                        coroutineScope.launch {
                                            updateTransacao(
                                                id = transaction.id,
                                                description = transaction.description,
                                                idSender = transaction.idSender,
                                                idReceiver = transaction.idReceiver,
                                                amount = transaction.amount,
                                                date = newDate,
                                                currency = transaction.currency,
                                                transacoesDao = transacoesDao
                                            )
                                            showDateDialog = false
                                        }
                                    }) {
                                        Text("Enviar")
                                    }
                                },
                                dismissButton = {
                                    androidx.compose.material3.TextButton(onClick = { showDateDialog = false }) {
                                        Text("Cancelar")
                                    }
                                },
                                title = { Text("Adiar Pagamento") },
                                text = {
                                    androidx.compose.material3.OutlinedTextField(
                                        value = newDate,
                                        onValueChange = { newDate = it },
                                        label = { Text("Nova data (DD/MM/YYYY)") }
                                    )
                                }
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}