package com.example.plantcare.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.plantcare.data.network.UserApiService

class UserViewModel(private val userApiService: UserApiService) : ViewModel() {

    // State for registration and login
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    // State for error messages
    val emailError = mutableStateOf("")
    val passwordError = mutableStateOf("")
    val confirmPasswordError = mutableStateOf("")
    val generalError = mutableStateOf("")

    fun registerUser() {
        // Reset error messages
        emailError.value = ""
        passwordError.value = ""
        confirmPasswordError.value = ""
        generalError.value = ""

        // Implement validation logic
        // ...

        // Make network call to register user
        // ...
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