package com.example.plantcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.ui.screens.LoginScreen
import com.example.plantcare.ui.screens.RegisterScreen
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.util.RetrofitService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantCareTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "register") {
                    composable("register") {
                        RegisterScreen(
                            userApiService = RetrofitService.userApi,
                            navigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }
                    /*composable("login") {
                        LoginScreen(
                            userApiService = yourApiServiceInstance,
                            navigateToRegister = {
                                navController.navigate("register")
                            }
                        )
                    }*/

                }
            }
        }
    }
}