package com.example.bankingapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankingapp.controllers.ProfileData
import com.example.bankingapp.lists.populateWithGenericProfiles
import com.example.bankingapp.lists.profileList
import com.example.bankingapp.lists.transactionList

@Composable
fun StatementScreen(user: ProfileData?) {
    // Filtra transações onde o usuário é remetente ou destinatário
    val transactions = remember {
        if (user == null) emptyList() else
            transactionList.filter { it.idSender == user.id || it.idReceiver == user.id }
                .sortedByDescending { it.date }
    }

    // Calcula saldo simples (entradas - saídas)
    val balance = remember {
        if (user == null) 0.0 else
            transactions.sumOf {
                when (user.id) {
                    it.idReceiver -> it.amount
                    it.idSender -> -it.amount
                    else -> 0.0
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
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
                Text(
                    "R$ %.2f".format(balance),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1976D2)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(transactions) { transaction ->
                // Descobre o outro participante
                val isSender = transaction.idSender == user?.id
                val otherId = if (isSender) transaction.idReceiver else transaction.idSender
                val otherProfile = profileList.find { it.id == otherId }
                val otherName = if (otherProfile != null)
                    "${otherProfile.firstName} ${otherProfile.lastName}"
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
                        Text(transaction.description ?: "", color = Color.DarkGray, fontSize = 13.sp)
                    }
                    Text(
                        amountPrefix + transaction.currency + " %.2f".format(transaction.amount),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = amountColor
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStatementScreen() {
    // Exemplo: mostra para o primeiro usuário da lista
    populateWithGenericProfiles()

    StatementScreen(user = profileList.firstOrNull())
}
