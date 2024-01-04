package com.example.plantcare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.R
import com.example.plantcare.data.model.DefaultImage
import com.example.plantcare.data.model.Plant
import com.example.plantcare.data.model.UserPlantDto
import com.example.plantcare.data.model.WaterRequirement
import com.example.plantcare.data.model.WateringTime
import com.example.plantcare.ui.components.PlantInfoRow
import com.example.plantcare.ui.components.TopBarWithBackButton
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.theme.SageGreen
import com.example.plantcare.ui.viewmodel.PlantViewModel
import com.example.plantcare.ui.viewmodel.UserPlantViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PlantDetailsScreen(
    plantId: String,
    plantViewModel: PlantViewModel,
    userPlantViewModel: UserPlantViewModel,
    navController: NavController
) {
    // Assuming you have a function in your ViewModel to fetch plant details
    val plant by plantViewModel.plantDetails.observeAsState()

    LaunchedEffect(plantId) {
        plantViewModel.getPlantDetails(plantId)
    }

    // If plant details have been fetched, display the content
    plant?.let {
        PlantDetailsContent(plant = it, userPlantViewModel = userPlantViewModel, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsContent(plant: Plant, userPlantViewModel: UserPlantViewModel, navController: NavController) {
    val gradientHeight = 100.dp
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.White),
        startY = 0f, // Gradient starts at the top of the spacer
        endY = with(LocalDensity.current) { gradientHeight.toPx() } // Gradient ends at the bottom of the spacer
    )

    val addPlantStatus by userPlantViewModel.addPlantStatus.observeAsState()

    LaunchedEffect(addPlantStatus) {
        addPlantStatus?.let { result ->
            if (result.isSuccess) {
                // Navigate to the home screen
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = plant.commonName ?: "", fontFamily = LexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column (modifier = Modifier.fillMaxSize()){
                Text(
                    text = plant.scientificName.firstOrNull() ?: "Unknown",
                    fontFamily = LexendFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )

                val imagePainter = rememberCoilPainter(
                    request = plant.defaultImage.mediumUrl ?: R.drawable.plantitem,
                    fadeIn = true,
                )
                //val imagePainter = painterResource(id = R.drawable.plantitem)
                Image(
                    painter = imagePainter,
                    contentDescription = "Plant image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )


                Spacer(Modifier.height(8.dp))

                // Scrollable column with fading effect
                val scrollState = rememberScrollState()
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth()
                            .padding(bottom = gradientHeight)
                    ) {

                    // Row of plant info icons
                    PlantInfoRow(plant = plant)

                    // Plant care level, type, etc.
                    StyledPlantText(
                        label = "Type: ",
                        content = plant.type,
                        labelColor = Color.Gray,
                        contentColor = Color.Black
                    )
                    StyledPlantText(
                        label = "Watering: ",
                        content = plant.watering,
                        labelColor = Color.Gray,
                        contentColor = Color.Black
                    )
                    StyledPlantText(
                        label = "Watering Time: ",
                        content = formatWateringTime(plant.wateringTime),
                        labelColor = Color.Gray,
                        contentColor = Color.Black
                    )
                    StyledPlantText(
                        label = "Watering Amount: ",
                        content = formatWaterRequirement(plant.depthWaterRequirement),
                        labelColor = Color.Gray,
                        contentColor = Color.Black
                    )

                    // Description and other details

                    Text(
                        text = plant.description,
                        modifier = Modifier.padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }

                    Spacer(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(gradientHeight)
                            .background(brush = gradientBrush)
                    )
                }


                Button(
                    onClick = {
                        //val scientificName = plant.scientificName.firstOrNull() ?: "Unknown"

                        // Access the value and unit directly from the WateringTime object
                        val wateringFrequency = plant.wateringTime?.value?.toIntOrNull() ?: 0 // Default to 0 if not available
                        val frequencyUnit = plant.wateringTime?.unit ?: "days" // Default to "days" if not available

                        val userPlantDto = UserPlantDto(
                            apiPlantId = plant.id,
                            scientificName = plant.commonName,
                            customName = null,
                            pictureUrl = plant.defaultImage.mediumUrl,
                            notificationsEnabled = false,
                            notificationTime = null, // or a user-defined time if you have one
                            wateringFrequency = wateringFrequency,
                            frequencyUnit = frequencyUnit
                        )
                        userPlantViewModel.addPlantToUserCollection(userPlantDto)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SageGreen),
                    modifier = Modifier
                        .padding( bottom = 16.dp) // Add padding to the top and bottom as needed
                        .defaultMinSize(minWidth = 200.dp)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally) // Center the button horizontally
                        .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Add Plant",
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Utility function to format watering time and amount
fun formatWateringTime(wateringTime: WateringTime?): String {
    // Check if wateringTime is not null
    return if (wateringTime != null) {
        // Assuming wateringTime has fields `value` and `unit`
        "${wateringTime.value} ${wateringTime.unit}"
    } else {
        "Unknown"
    }
}

fun formatWaterRequirement(waterRequirements: List<WaterRequirement>?): String {
    // Check if waterRequirements is not null and not empty
    return if (!waterRequirements.isNullOrEmpty()) {
        // Assuming you want to format the first waterRequirement in the list
        val waterRequirement = waterRequirements.first()
        "${waterRequirement.value} ${waterRequirement.unit}"
    } else {
        "Unknown"
    }
}

@Composable
fun StyledPlantText(label: String, content: String, labelColor: Color, contentColor: Color) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = labelColor, fontWeight = FontWeight.SemiBold)) {
            append(label)
        }
        withStyle(style = SpanStyle(color = contentColor, fontWeight = FontWeight.Light)) {
            append(content)
        }
    }
    Text(
        text = annotatedString,
        fontFamily = LexendFontFamily,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textAlign = TextAlign.Center
    )
}

/*@Preview(showBackground = true)
@Composable
fun PlantDetailsScreenPreview() {
    // Create a mock Plant object with sample data for preview
    val samplePlant = Plant(
        id = "1",
        commonName = "Monstera",
        scientificName = listOf("Monstera Deliciosa"),
        type = "Tropical",
        indoor = true,
        watering = "Once a week",
        defaultImage = DefaultImage(mediumUrl = ""),
        careLevel = "Low",
        description = "This is a sample description of the plant. dfgdfg sdfsdfsdfsdsdfd  d sd  sdf sd s df s fgd on of the plant. dfgdfg sdfsdfsdfsdsdfd  d sd  sdf sd s df s fgd dfgdfg sdfsdfsdfsdsdfd  d sd  sdf sd s df s fgd on of the plant. dfgdfg sdfsdfsdfsdsdfd  d sd  sdf sd s df s fgd",
        sunlight = setOf("full_sun", "part_shade"),
        depthWaterRequirement = listOf(WaterRequirement(unit = "inches", value = 5)),
        wateringTime = WateringTime(value = "5", unit = "days"),
        imageUrl = ""
    )
    PlantDetailsContent(plant = samplePlant, navController = rememberNavController())
}*/

