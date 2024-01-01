package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.plantcare.ui.components.BottomBar
import com.example.plantcare.ui.components.PlantItem
import com.example.plantcare.ui.components.TopBarWithSettings
import com.example.plantcare.ui.viewmodel.UserPlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: UserPlantViewModel, navController: NavController) {
    /*val userPlants by viewModel.userPlants.observeAsState(listOf()) // Assuming this is a LiveData or StateFlow

    Scaffold(
        topBar = { TopBarWithSettings { navController.navigate("settings") } },
        bottomBar = { BottomBar { destination -> navController.navigate(destination) } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(userPlants) { plant ->
                    PlantItem(plant)
                }
            }
        }
    }*/
}