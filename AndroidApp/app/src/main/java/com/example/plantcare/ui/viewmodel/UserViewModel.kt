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
        emailError.value = ""
        passwordError.value = ""
        confirmPasswordError.value = ""
        generalError.value = ""

        // Input validation (pseudo code)
        val isInputValid = email.value.isNotBlank() &&
                password.value.isNotBlank() &&
                confirmPassword.value == password.value
        // Add more validation as needed

        if (isInputValid) {
            if (isEmailValid(email.value) && isPasswordStrong(password.value)) {
                // Make network call
                val userDto = UserDto(email.value, password.value, confirmPassword.value)
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
                // Set error messages
                if (email.value.isBlank()) emailError.value = "Email is required"
                if (password.value.isBlank()) passwordError.value = "Password is required"
                if (confirmPassword.value != password.value) confirmPasswordError.value =
                    "Passwords do not match"
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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



    // Function to handle user login
    /*fun loginUser() {
        // Reset error messages
        emailError.value = ""
        passwordError.value = ""
        generalError.value = ""

        // Implement validation logic
        // ...

        // Make network call to log in user
        // ...
    }*/
}