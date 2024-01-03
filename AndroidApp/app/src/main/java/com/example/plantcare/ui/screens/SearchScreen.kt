package com.example.plantcare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plantcare.R
import com.example.plantcare.data.model.SimplePlant
import com.example.plantcare.ui.components.BottomBar
import com.example.plantcare.ui.components.PlantItem
import com.example.plantcare.ui.components.TopBarWithSettings
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.viewmodel.PlantViewModel
import com.google.accompanist.coil.rememberCoilPainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun SearchScreen(viewModel: PlantViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    val searchHistory by viewModel.recentlySearched.observeAsState(emptyList())

    Scaffold(
        topBar = { TopBarWithSettings { navController.navigate("settings") } },
        bottomBar = { BottomBar { destination -> navController.navigate(destination) } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {


            Column {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        viewModel.searchPlants(query)
                    },
                    label = { Text("Search for plants...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true
                )

                // Conditionally display search results or history
                when {
                    searchQuery.isNotEmpty() && searchResults.isEmpty() -> {
                        // No results found for the search query
                        Text(
                            "No results found for \"$searchQuery\"",
                            fontFamily = LexendFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    searchQuery.isNotEmpty() -> {
                        // Display search results
                        LazyColumn {
                            items(searchResults) { plant ->
                                SearchResultItem(plant, navController)
                            }
                        }
                    }

                    else -> {
                        // Display search history
                        Text(
                            "History",
                            fontFamily = LexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                        LazyColumn {
                            items(searchHistory) { historyItem ->
                                PlantItem(
                                    plantName = historyItem.plantName,
                                    imageUrl = historyItem.plantImageUrl,
                                    onClick = { navController.navigate("plantDetails/${historyItem.apiPlantId}") }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

    @Composable
    fun SearchResultItem(plant: SimplePlant, navController: NavController) {
        val imagePainter = if (plant.imageUrl.isEmpty()) {
            painterResource(id = R.drawable.plantitem) // Default image
        } else {
            rememberCoilPainter(request = plant.imageUrl)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("plantDetails/${plant.id}") }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = imagePainter,
                contentDescription = plant.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(plant.name, fontWeight = FontWeight.Bold)
                Text(plant.scientificName ?: "", fontStyle = FontStyle.Italic)
            }
        }
    }
