package com.example.plantcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.ui.screens.LoginScreen
import com.example.plantcare.ui.screens.RegisterScreen
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.ui.viewmodel.UserViewModel
import com.example.plantcare.util.RetrofitService
import com.example.plantcare.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)

        setContent {
            val navController = rememberNavController()
            val startDestination = if (sessionManager.isLoggedIn) "home" else "login"

            PlantCareTheme {
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        val loginViewModel = UserViewModel(RetrofitService.userApi)
                        LoginScreen(
                            viewModel = loginViewModel,
                            navigateToRegistration = { navController.navigate("register") },
                            navigateToMain = {
                                sessionManager.isLoggedIn = true
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("register") {
                        val registerViewModel = UserViewModel(RetrofitService.userApi)
                        RegisterScreen(
                            viewModel = registerViewModel,
                            navigateToLogin = { navController.navigate("login") }
                        )
                    }
                    /*composable("home") {
                        HomeScreen(
                            navigateToLogin = {
                                sessionManager.isLoggedIn = false
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    } */
                    // Add other composables for authenticated users
                }
            }
        }
    }
}