package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onLogin = { email, password -> /* Handle login */ },
        navigateToRegister = { /* Navigate to register */ }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, navigateToRegister: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    MiddleSection {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Login button
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Link to registration screen
            TextButton(onClick = navigateToRegister) {
                Text("Don't have an account? Register here")
            }
        }
    }
}

@Composable
fun MiddleSection(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .height(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        shape = RoundedCornerShape(28.dp)
    ) {
        content()
    }
}