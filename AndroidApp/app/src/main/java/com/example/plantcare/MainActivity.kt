package com.example.plantcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.ui.screens.HomeScreen
import com.example.plantcare.ui.screens.LoginScreen
import com.example.plantcare.ui.screens.PlantDetailsScreen
import com.example.plantcare.ui.screens.RegisterScreen
import com.example.plantcare.ui.screens.SearchScreen
import com.example.plantcare.ui.screens.UserPlantDetailsScreen
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.ui.viewmodel.PlantViewModel
import com.example.plantcare.ui.viewmodel.UserPlantViewModel
import com.example.plantcare.ui.viewmodel.UserViewModel
import com.example.plantcare.util.RetrofitService
import com.example.plantcare.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val userPlantViewModel: UserPlantViewModel by viewModels()
    private val plantViewModel: PlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            PlantCareTheme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(userViewModel, navController)
                    }
                    composable("register") {
                        RegisterScreen(userViewModel, navController)
                    }
                    composable("home") {
                        HomeScreen(userPlantViewModel, navController)
                    }
                    composable("search") {
                        SearchScreen(plantViewModel, navController)
                    }
                    composable("plantDetails/{plantId}") { backStackEntry ->
                        PlantDetailsScreen(
                            plantId = backStackEntry.arguments?.getString("plantId") ?: "",
                            plantViewModel = plantViewModel,
                            userPlantViewModel = userPlantViewModel,
                            navController = navController
                        )
                    }
                    composable("{userPlantId}") { backStackEntry ->
                        UserPlantDetailsScreen(
                            userPlantId = backStackEntry.arguments?.getString("userPlantId") ?: "",
                            viewModel = userPlantViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

