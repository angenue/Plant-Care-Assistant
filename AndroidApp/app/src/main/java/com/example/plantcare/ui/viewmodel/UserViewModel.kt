package com.example.plantcare.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.plantcare.data.model.User
import com.example.plantcare.data.model.UserDto
import com.example.plantcare.data.network.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Base64;
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userApiService: UserApiService) : ViewModel() {

    // State for registration and login
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    // State for error messages
    val emailError = mutableStateOf("")
    val passwordError = mutableStateOf("")
    val confirmPasswordError = mutableStateOf("")
    val generalError = mutableStateOf("")

    fun registerUser(onRegistrationSuccess: () -> Unit) {
        // Reset errors
        val trimmedEmail = email.value.trim()
        val trimmedPassword = password.value.trim()
        val trimmedConfirmPassword = confirmPassword.value.trim()

        // Reset errors
        emailError.value = ""
        passwordError.value = ""
        confirmPasswordError.value = ""
        generalError.value = ""


        val isEmailValid = isEmailValid(trimmedEmail)
        val isPasswordStrong = isPasswordStrong(trimmedPassword)
        val doPasswordsMatch = trimmedPassword == trimmedConfirmPassword

        if (!doPasswordsMatch) {
            confirmPasswordError.value = "Passwords do not match"
        }

        val isInputValid = isEmailValid && isPasswordStrong && doPasswordsMatch

        if (isInputValid) {
                // Make network call
                val userDto = UserDto(trimmedEmail, trimmedPassword, trimmedConfirmPassword)
                val call = userApiService.registerUser(userDto)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            onRegistrationSuccess()
                        } else {
                            generalError.value =
                                "Registration failed: ${response.errorBody()?.string()}"
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        generalError.value = "Network error: ${t.message}"
                    }
                })
            } else {
            if (trimmedEmail.isBlank()) emailError.value = "Email is required"
            if (trimmedPassword.isBlank()) passwordError.value = "Password is required"
            if (trimmedConfirmPassword.isBlank()) confirmPasswordError.value = "Password is required"
            }
        }


    private fun isEmailValid(email: String): Boolean {
        if (email.isBlank()) {
            emailError.value = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Invalid email format"
            println("hello");
            return false
        }

        return true
    }


    private fun isPasswordStrong(password: String): Boolean {
        if (password.length < 8) {
            passwordError.value = "Password must be at least 8 characters long"
            return false
        }

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

        if (!(hasUpper && hasLower && hasDigit && hasSpecial)) {
            passwordError.value = "Password must contain uppercase, lowercase, digit, and special character"
            return false
        }

        return true
    }


    fun loginUser(onLoginSuccess: () -> Unit) {
        val trimmedEmail = email.value.trim()
        val trimmedPassword = password.value.trim()

        // Reset previous errors
        emailError.value = ""
        passwordError.value = ""
        generalError.value = ""

        // Check if the email and password fields are not empty
        var isFormValid = true
        if (trimmedEmail.isEmpty()) {
            emailError.value = "Email is required"
            isFormValid = false
        }
        if (trimmedPassword.isEmpty()) {
            passwordError.value = "Password is required"
            isFormValid = false
        }

        // If the form is valid, proceed with the login
        if (isFormValid) {
            // Encode credentials and create the Authorization header
            val credentials = "$trimmedEmail:$trimmedPassword"
            val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            // Perform the login request using Retrofit
            userApiService.loginUser(authHeader).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        onLoginSuccess()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        try {
                            val jsonObject = JSONObject(errorBody)
                            val message = jsonObject.getString("message")
                            generalError.value = message // Set the error message from JSON
                        } catch (e: JSONException) {
                            // If parsing fails, fallback to the full error body or a default error message
                            generalError.value = errorBody ?: "An unknown error occurred"
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle network errors or other unexpected errors
                    generalError.value = t.message ?: "Network error"
                }
            })
        }
    }
}