package com.example.plantcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.ui.screens.RegisterScreen
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.ui.viewmodel.UserViewModel
import com.example.plantcare.util.RetrofitService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel = UserViewModel(RetrofitService.userApi)
            PlantCareTheme {

                NavHost(navController = navController, startDestination = "register") {
                    composable("register") {
                        RegisterScreen(
                            viewModel = authViewModel,
                            navigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                }
            }
        }
    }
}