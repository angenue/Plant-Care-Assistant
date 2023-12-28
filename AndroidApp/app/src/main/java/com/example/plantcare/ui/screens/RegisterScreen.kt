package com.example.plantcare.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.data.model.User
import com.example.plantcare.data.model.UserDto
import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.ui.components.LoginRegisterBackground
import com.example.plantcare.ui.theme.SageGreen
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/*@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    LoginRegisterBackground().MyApp {
        RegisterScreen(
            onRegister = { _, _, _ -> },
            navigateToLogin = {}
        )
    }
}*/

@Composable
fun RegisterScreenWithBackground(userApiService: UserApiService, navigateToLogin: () -> Unit) {
    LoginRegisterBackground().MyApp {
        RegisterScreen(userApiService, navigateToLogin)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(userApiService: UserApiService, navigateToLogin: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }

    LoginRegisterBackground().MiddleSection("Register") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError.isNotEmpty(),
                singleLine = true
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.isNotEmpty(),
                singleLine = true
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("confirmPassword") },
                modifier = Modifier.fillMaxWidth(),
                isError = confirmPasswordError.isNotEmpty(),
                singleLine = true
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(confirmPasswordError, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Clear previous error messages
                    emailError = ""
                    passwordError = ""
                    confirmPasswordError = ""

                    // Validate the input fields
                    val isInputValid = validateInputs(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        setEmailError = { emailError = it },
                        setPasswordError = { passwordError = it },
                        setConfirmPasswordError = { confirmPasswordError = it }
                    )

                    // If the input is valid, proceed with registration
                    if (isInputValid) {
                        registerUser(email, password, confirmPassword, userApiService) { success, message ->
                            if (success) {
                                // TODO: log in user instead of navigating to login
                                navigateToLogin()
                            } else {
                                generalError = message
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SageGreen, // Button background color
                    contentColor = MaterialTheme.colorScheme.onPrimary // Text color on button
                )
            ) {
                Text("Register")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = navigateToLogin) {
                Text("Already have an account? Log in")
            }
        }
    }
}

private fun validateInputs(
    email: String,
    password: String,
    confirmPassword: String,
    setEmailError: (String) -> Unit,
    setPasswordError: (String) -> Unit,
    setConfirmPasswordError: (String) -> Unit
): Boolean {
    var isValid = true

    // Check if email field is empty
    if (email.isBlank()) {
        setEmailError("Email is required")
        isValid = false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        setEmailError("Invalid email format")
        isValid = false
    } else {
        setEmailError("")
    }

    // Check if password field is empty
    if (password.isBlank()) {
        setPasswordError("Password is required")
        isValid = false
    } else if (!isPasswordStrong(password)) {
        setPasswordError("Password must be at least 8 characters long and include uppercase, lowercase, a digit, and a special character.")
        isValid = false
    } else {
        setPasswordError("")
    }

    // Check if confirm password field is empty
    if (confirmPassword.isBlank()) {
        setConfirmPasswordError("Confirm Password is required")
        isValid = false
    } else if (password != confirmPassword) {
        setConfirmPasswordError("Passwords do not match")
        isValid = false
    } else {
        setConfirmPasswordError("")
    }

    return isValid
}

private fun isPasswordStrong(password: String): Boolean {
    if (password.length < 8) return false

    var hasUpper = false
    var hasLower = false
    var hasDigit = false
    var hasSpecial = false

    for (c in password) {
        when {
            c.isUpperCase() -> hasUpper = true
            c.isLowerCase() -> hasLower = true
            c.isDigit() -> hasDigit = true
            !c.isLetterOrDigit() -> hasSpecial = true
        }
    }

    return hasUpper && hasLower && hasDigit && hasSpecial
}


private fun registerUser(email: String, password: String, confirmPassword: String, userApiService: UserApiService, onResult: (Boolean, String) -> Unit) {
    val userDto = UserDto(email, password, confirmPassword)
    val call = userApiService.registerUser(userDto)

    call.enqueue(object : retrofit2.Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                // Registration successful
                onResult(true, "Registration successful")
            } else {
                // Server returned an error
                val errorMessage = when (response.code()) {
                    400 -> "Invalid request"
                    409 -> "Email already taken"
                    else -> "Unknown error occurred"
                }
                onResult(false, errorMessage)
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            // Network error or exception occurred
            onResult(false, "Network error: ${t.message}")
        }
    })
}

