package com.example.plantcare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.plantcare.data.model.Plant
import com.example.plantcare.ui.theme.SageGreen

@Composable
fun PlantInfoRow(plant: Plant) {
    Row(
        modifier = Modifier.run {
            fillMaxWidth()
                .padding(16.dp)
        },
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Care Level
        PlantInfoIcon(
            icon = when (plant.careLevel.lowercase()) {
                "low" -> Icons.Default.ThumbUp
                "medium" -> Icons.Default.ThumbsUpDown
                "high" -> Icons.Default.ThumbDown
                else -> Icons.Default.HelpOutline // Null or unknown
            },
            text = "Care: ${plant.careLevel}"
        )

        // Indoor/Outdoor
        PlantInfoIcon(
            icon = if (plant.indoor) Icons.Default.Home else Icons.Default.Park,
            text = if (plant.indoor) "Indoor" else "Outdoor"
        )

        // Sunlight
        PlantInfoIcon(
            icon = when {
                "full_sun" in plant.sunlight -> Icons.Default.WbSunny
                "part_shade" in plant.sunlight -> Icons.Default.FilterDrama
                "full_shade" in plant.sunlight -> Icons.Default.Cloud
                else -> Icons.Default.WbCloudy // Default or multiple sunlight types
            },
            text = plant.sunlight.joinToString(", ")
        )
    }
}

@Composable
fun PlantInfoIcon(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .shadow(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Column(modifier = Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = SageGreen)
            Text(text, textAlign = TextAlign.Center)
        }
    }
}