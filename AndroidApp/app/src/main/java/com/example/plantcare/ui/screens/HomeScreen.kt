package com.example.plantcare.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.plantcare.ui.components.BottomBar
import com.example.plantcare.ui.components.TopBarWithSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
// This is your navigation logic or a placeholder for your navigation controller
    val onNavigate: (String) -> Unit = { destination ->
        // Handle navigation based on 'destination'
        // This will depend on your app's navigation setup
    }

    Scaffold(
        topBar = { TopBarWithSettings (onSettingsClick ={ navController.navigate("settings") })
        },
        bottomBar = { BottomBar(onNavigate) }
    ) { innerPadding ->
        // Use 'innerPadding' to ensure content is not obscured by the BottomBar.
        Box(modifier = Modifier.padding(innerPadding)) {




            // Your screen content here
        }
    }
}