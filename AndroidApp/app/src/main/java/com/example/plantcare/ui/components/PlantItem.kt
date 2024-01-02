package com.example.plantcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plantcare.R
import com.example.plantcare.data.model.CombinedPlantDto
import com.example.plantcare.data.model.UserPlant
import com.example.plantcare.ui.theme.LexendFontFamily
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PlantItem(userPlant: UserPlant, navController: NavController) {
    val placeholderImage = painterResource(id = R.drawable.plantitem)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.8f)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate("plantDetails/${userPlant.userId}")
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .clip(RoundedCornerShape(20.dp))) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            ) {


                val imagePainter = if (userPlant.pictureUrl.isNullOrEmpty()) {
                    placeholderImage
                } else {
                    rememberCoilPainter(
                        request = userPlant.pictureUrl,
                        fadeIn = true
                    )
                }

                Image(
                    painter = imagePainter,
                    contentDescription = userPlant.customName ?: "Plant",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = userPlant.customName ?: "Unnamed Plant",
                fontFamily = LexendFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}


