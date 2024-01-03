package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.plantcare.ui.components.BottomBar
import com.example.plantcare.ui.components.PlantItem
import com.example.plantcare.ui.components.TopBarWithSettings
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.viewmodel.UserPlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: UserPlantViewModel,
            navController: NavController
) {
    // Observe the LiveData from ViewModel
    val userPlants by viewModel.userPlants.observeAsState(emptyList())

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"


    Scaffold(
        topBar = { TopBarWithSettings { navController.navigate("settings") } },
        bottomBar = { BottomBar(currentRoute = currentRoute) { destination -> navController.navigate(destination) } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (userPlants.isEmpty()) {
                // Display message when there are no plants saved
                NoPlantsMessage()
            } else {
                Column {
                    Text(
                        "Saved Plants",
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 170.dp),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        items(userPlants) { userPlant ->
                            PlantItem(
                                plantName = userPlant.customName,
                                imageUrl = userPlant.pictureUrl,
                                onClick = { navController.navigate("plantDetails/${userPlant.userId}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoPlantsMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You don't have any saved plants.",
            fontFamily = LexendFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
        Text(
            text = "To save a plant, press the '+' button below.",
            fontFamily = LexendFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val mockNavController = rememberNavController()

    // Mock data provider lambda
    val mockUserPlantsProvider = {
        MutableLiveData<List<UserPlant>>(
            listOf(
                UserPlant(1L, "plant1", "Fern", null, true, LocalTime.of(9, 0), 3, "days"),
                UserPlant(2L, "plant2", "Cactus", null, false, LocalTime.of(15, 30), 7, "days"),
                UserPlant(
                    userId = 1L,
                    apiPlantId = "plant3",
                    customName = null, // No custom name
                    pictureUrl = null, // No custom picture URL
                    notificationsEnabled = true,
                    notificationTime = LocalTime.of(18, 0), // 6:00 PM
                    wateringFrequency = 2,
                    frequencyUnit = "weeks"
                )
                // Add more mock UserPlants as needed
            )
        ) as LiveData<List<UserPlant>>
    }

    HomeScreen(userPlantsProvider = mockUserPlantsProvider, navController = mockNavController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userPlantsProvider: () -> LiveData<List<UserPlant>>, navController: NavController) {
    val userPlants by userPlantsProvider().observeAsState(emptyList())


    Scaffold(
        topBar = { TopBarWithSettings { navController.navigate("settings") } },
        bottomBar = { BottomBar { destination -> navController.navigate(destination) } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                Text(
                    "Saved Plants",
                    fontFamily = LexendFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(userPlants) { userPlant ->
                        PlantItem(userPlant, navController)
                    }
                }
            }
        }
    }
}*/



