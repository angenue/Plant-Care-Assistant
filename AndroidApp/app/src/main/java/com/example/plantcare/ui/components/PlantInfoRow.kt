package com.example.plantcare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.data.model.Plant
import com.example.plantcare.ui.theme.ArmyGreen
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.theme.SageGreen
import java.util.Locale

@Composable
fun PlantInfoRow(plant: Plant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Care Level
        PlantInfoIcon(
            icon = when (plant.careLevel?.lowercase(Locale.getDefault()) ?: "") {
                "low" -> Icons.Default.ThumbUp
                "medium" -> Icons.Default.ThumbsUpDown
                "moderate" -> Icons.Default.ThumbsUpDown
                "high" -> Icons.Default.ThumbDown
                else -> Icons.Default.HelpOutline // Null or unknown
            },
            text = "Care: ${plant.careLevel ?: "Unknown"}"
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
            .size(100.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)

    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally in column
                verticalArrangement = Arrangement.Center, // Center vertically in column
                modifier = Modifier
                    .fillMaxSize() // Fill the size of the card
                    .padding(8.dp) // Padding inside the card
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.dp)
                        .align(Alignment.CenterHorizontally), // Center the icon horizontally
                    tint = ArmyGreen
                )
                Text(
                    text,
                    textAlign = TextAlign.Center,
                    fontFamily = LexendFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text horizontally
                )
            }
        }
    }
}