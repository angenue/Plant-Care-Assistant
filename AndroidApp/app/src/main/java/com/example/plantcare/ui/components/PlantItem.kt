package com.example.plantcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.plantcare.data.model.CombinedPlantDto

@Composable
fun PlantItem(plant: CombinedPlantDto) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        val image: ImageBitmap = loadImageBitmap(plant.userPlantDetails.pictureUrl ?: plant.plantDetails.imageUrl)

        Image(
            bitmap = image,
            contentDescription = plant.userPlantDetails.customName ?: plant.plantDetails.commonName,
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f)
        )
        Text(plant.userPlantDetails.customName ?: plant.plantDetails.commonName)
    }
}


fun loadImageBitmap(imageUri: String): ImageBitmap {
    // Load and return the image bitmap from the URI
    // Placeholder implementation
    return ImageBitmap(100, 100) // Replace with actual image loading logic
}
