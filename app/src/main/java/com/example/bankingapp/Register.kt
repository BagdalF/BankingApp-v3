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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(onRegister: (firstName: String, lastName: String, email: String, phone: String, password: String, errorMessage: () -> Unit) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Please sign in to continue",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

                Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onRegister(firstName, lastName, email, phone, password) { errorMessage = "Invalid credentials." }
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
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}