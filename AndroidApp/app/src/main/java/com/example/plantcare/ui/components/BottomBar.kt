package com.example.plantcare.ui.components


import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import com.example.plantcare.ui.theme.SageGreen


@Composable
fun BottomBar(currentRoute: String,onNavigate: (String) -> Unit) {
    val selectedItem = remember { mutableStateOf(currentRoute) }

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem.value == "home",
            onClick = {
                selectedItem.value = "home"
                onNavigate("home")
            }
        )
        // The camera button can be a FloatingActionButton for emphasis if desired
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Camera"
                )
            },
            label = { Text("Camera") },
            selected = selectedItem.value == "camera",
            onClick = {
                selectedItem.value = "camera"
                onNavigate("camera")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = selectedItem.value == "search",
            onClick = {
                selectedItem.value = "search"
                onNavigate("search")
            }
        )
    }
}
