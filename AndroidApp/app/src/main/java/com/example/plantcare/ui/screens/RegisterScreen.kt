package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.ui.components.LoginRegisterBackground
import com.example.plantcare.ui.components.PasswordTextField
import com.example.plantcare.ui.theme.SageGreen
import com.example.plantcare.ui.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: UserViewModel, navigateToLogin: () -> Unit) {
    val email by viewModel.email
    val password by viewModel.password
    val confirmPassword by viewModel.confirmPassword

    val emailError by viewModel.emailError
    val passwordError by viewModel.passwordError
    val confirmPasswordError by viewModel.confirmPasswordError
    val generalError by viewModel.generalError

    LoginRegisterBackground().MyApp {
        LoginRegisterBackground().MiddleSection("Register") {
            Column(modifier = Modifier.padding(16.dp)
                .fillMaxWidth(),
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

                // Confirm Password TextField
                PasswordTextField(
                    password = confirmPassword,
                    onPasswordChange = { viewModel.confirmPassword.value = it },
                    label = "Confirm Password",
                    isError = confirmPasswordError.isNotEmpty(),
                    errorMessage = confirmPasswordError
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.registerUser(onRegistrationSuccess = navigateToLogin) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SageGreen, // Button background color
                        contentColor = MaterialTheme.colorScheme.onPrimary // Text color on button
                    )
                ) {
                    Text("Register")
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
                TextButton(onClick = navigateToLogin) {
                    Text("Already have an account? Log in")
                }
            }
        }
    }
}

