package com.example.plantcare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class LoginRegisterBackground {
    @Preview
    @Composable
    fun MyApp() {
        MaterialTheme {
            // Box used for the gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                Color.LightGray // Light gray color at the bottom
                            ),

                        )
                    )
            ) {
                // Content of your app goes here
                TopSection()
                BottomSection()
            }
        }
    }

    @Preview
    @Composable
    fun TopSection() {
        // Top section with the plant image or any other UI elements that are common
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Adjust size as necessary
            contentAlignment = Alignment.Center
        ) {
            // Replace with an actual Image composable and your image resource
            Text("Top Section Placeholder")
        }
    }

    @Preview
    @Composable
    fun BottomSection() {
        // Bottom section with decorative graphics
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // Adjust size as necessary
            contentAlignment = Alignment.Center
        ) {
            // Replace with actual graphics or composables
            Text("Bottom Section Placeholder")
        }
    }
}