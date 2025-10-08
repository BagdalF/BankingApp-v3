package com.example.bankingapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankingapp.data.Usuarios

// Data class for investment tips
data class TipInvestment(
    val title: String,
    val desc: String,
)

@Composable
fun HomeScreen(currentUser: Usuarios) {

    val listOfTips = listOf(
        TipInvestment("Seja Bem-vindo!", "Ficamos muitos felizes por ter você conosco!"),
        TipInvestment(
            "Invista agora no seu CDB 130%",
            "Investindo no máximo 3000, você pode ter um rendimento de 130% durante 5 anos"
        ),
        TipInvestment(
            "Indique 5 amigos e ganhe um premio!",
            "Indique 5 amigos e cada um invista 5000, libere um cdb de 400%"
        ),
        TipInvestment("Seja Cliente VIP!", "Sendo cliente VIP, você libera até 10000 de limite."),
        TipInvestment(
            "Seja Cliente PREMIUM!",
            "Sendo cliente PREMIUM, você libera até 50000 de limite."
        ),
        TipInvestment(
            "Seja Cliente BLACK!",
            "Sendo cliente BLACK, você recebe um cartão ilimitado."
        ),
    )

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(50.dp)
                            .padding(0.dp, 0.dp, 1.dp, 0.dp)
                    )
                }
                Text(
                    "${currentUser.firstName} ${currentUser.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        ListofTipsRow(listOfTips)
        BalanceCard()
    }
}

@Composable
fun ListofTipsRow(tips: List<TipInvestment>) {
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp, 16.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        items(tips) { tip ->
            CardTips(tip)
        }
    }
}

@Composable
fun CardTips(tip: TipInvestment) {
    Card(
        modifier = Modifier.width(300.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(tip.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(tip.desc, fontSize = 16.sp)
        }
    }
}

@Composable
fun BalanceCard() {
    var showBalance by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Saldo disponivel:", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Text(
                text = if (showBalance) "R$ 293,42" else "****", // Substituir por valor real
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (showBalance) "Ocultar" else "Mostrar",
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        showBalance = !showBalance
                    }
            )
        }
    }
}
