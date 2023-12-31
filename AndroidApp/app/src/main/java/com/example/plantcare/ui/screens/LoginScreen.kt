package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plantcare.ui.components.LoginRegisterBackground
import com.example.plantcare.ui.components.PasswordTextField
import com.example.plantcare.ui.theme.SageGreen
import com.example.plantcare.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: UserViewModel, navController: NavController) {
    val email by viewModel.email
    val password by viewModel.password

    val emailError by viewModel.emailError
    val passwordError by viewModel.passwordError
    val generalError by viewModel.generalError

    LoginRegisterBackground().MyApp {
        LoginRegisterBackground().MiddleSection("Login") {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.email.value = it },
                    label = { Text("Email") },
                    isError = emailError.isNotEmpty()
                )
                if (emailError.isNotEmpty()) {
                    Text(emailError, color = Color.Red, fontSize = 12.sp)
                }

                // Password TextField
                PasswordTextField(
                    password = password,
                    onPasswordChange = { viewModel.password.value = it },
                    label = "Password",
                    isError = passwordError.isNotEmpty(),
                    errorMessage = passwordError
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                            viewModel.loginUser(onLoginSuccess = {navController.navigate("home") {
                                // Clear the back stack so users can't navigate back to the login screen
                                popUpTo("login") { inclusive = true }
                            }})
                    },
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (generalError.isNotEmpty()) {
                    Text(
                        text = generalError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                TextButton(onClick =  { navController.navigate("register") }) {
                    Text("Don't have an account? Register")
                }
            }
        }
    }
}
