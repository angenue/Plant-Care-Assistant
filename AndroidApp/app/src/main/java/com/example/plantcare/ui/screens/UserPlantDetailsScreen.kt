package com.example.plantcare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.plantcare.data.model.Plant
import com.example.plantcare.ui.components.PlantInfoRow
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.theme.SageGreen
import com.example.plantcare.ui.viewmodel.OperationStatus
import com.example.plantcare.ui.viewmodel.UserPlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPlantDetailsScreen(
    userPlantId: String,
    viewModel: UserPlantViewModel,
    navController: NavController
) {
    // Assuming you have a function in your ViewModel to fetch plant details
    val userPlant by viewModel.plantDetails.observeAsState()
    val updateStatus by viewModel.updatePlantStatus.observeAsState()
    val deleteStatus by viewModel.deletePlantStatus.observeAsState()

    // Local state for editing
    var isEditing by remember { mutableStateOf(false) }
    var editableCustomName by remember(userPlant) { mutableStateOf(userPlant?.userPlantDetails?.customName.orEmpty()) }
    var editableImageUri by remember(userPlant) { mutableStateOf(userPlant?.userPlantDetails?.pictureUrl.orEmpty()) }

    LaunchedEffect(userPlantId) {
        viewModel.getPlantDetails(userPlantId.toLong())
    }


    deleteStatus?.let { status ->
        // Handle delete status
        if (status is OperationStatus.Success && status.data) {
            // Navigate back to home screen if delete is successful
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId)
            }
        }
    }

    val gradientHeight = 100.dp
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.White),
        startY = 0f, // Gradient starts at the top of the spacer
        endY = with(LocalDensity.current) { gradientHeight.toPx() } // Gradient ends at the bottom of the spacer
    )


    Scaffold(
        topBar = {
            // Custom top bar with back navigation and title
            TopAppBarWithBackButton(
                title = userPlant?.userPlantDetails?.scientificName,
                onBackClick = {
                    navController.popBackStack()
                })
        },
        floatingActionButton = {
            if (isEditing) {
                FloatingActionButton(onClick = {
                    isEditing = false
                    // Call ViewModel functions to update the plant
                    viewModel.updatePlantName(userPlantId.toLong(), editableCustomName)
                    if (editableImageUri.isNotEmpty()) {
                        viewModel.updateUserPlantPicture(userPlantId.toLong(), editableImageUri)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                }
            } else {
                FloatingActionButton(onClick = {
                    isEditing = true
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                userPlant?.let { combinedPlant ->
                    if (isEditing) {
                        TextField(
                            value = editableCustomName,
                            onValueChange = { editableCustomName = it },
                            label = { Text(text="Custom Name", fontFamily = LexendFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                                    .align(Alignment.CenterHorizontally)) }
                        )
                        EditablePlantImage(editableImageUri) { newUri ->
                            editableImageUri = newUri
                        }
                    } else {
                        Text(editableCustomName.ifEmpty { "No Custom Name" })
                        PlantImage(editableImageUri)
                    }

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
                            PlantInfoRow(plant = combinedPlant.plantDetails)

                            // Plant care level, type, etc.
                            StyledPlantText(
                                label = "Type: ",
                                content = combinedPlant.plantDetails.type,
                                labelColor = Color.Gray,
                                contentColor = Color.Black
                            )
                            StyledPlantText(
                                label = "Watering: ",
                                content = combinedPlant.plantDetails.watering,
                                labelColor = Color.Gray,
                                contentColor = Color.Black
                            )
                            StyledPlantText(
                                label = "Watering Time: ",
                                content = formatWateringTime(combinedPlant.plantDetails.wateringTime),
                                labelColor = Color.Gray,
                                contentColor = Color.Black
                            )
                            StyledPlantText(
                                label = "Watering Amount: ",
                                content = formatWaterRequirement(combinedPlant.plantDetails.depthWaterRequirement),
                                labelColor = Color.Gray,
                                contentColor = Color.Black
                            )

                            // Description and other details

                            Text(
                                text = combinedPlant.plantDetails.description,
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
                            viewModel.deleteUserPlant(userPlantId.toLong())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SageGreen),
                        modifier = Modifier
                            .padding(bottom = 16.dp) // Add padding to the top and bottom as needed
                            .defaultMinSize(minWidth = 200.dp)
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally) // Center the button horizontally
                            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Delete Plant")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditablePlantImage(imageUri: String, onImageSelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        // Implement your image picker dialog here.
        // For example, this could be a dialog that allows picking an image from the gallery.
        // After picking an image, you should call onImageSelected with the new image URI.
    }

    Box(contentAlignment = Alignment.BottomEnd) {
        // Display the image
        Image(
            painter = rememberImagePainter(data = imageUri),
            contentDescription = "Plant Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        // Overlay icon to change the image
        IconButton(onClick = { showDialog = true }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Change Image",
                modifier = Modifier.background(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = CircleShape
                )
            )
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantImage(imageUri: String) {
    Image(
        painter = rememberImagePainter(data = imageUri),
        contentDescription = "Plant Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarWithBackButton(title: String?, onBackClick: () -> Unit) {
        TopAppBar(
            title = { Text(text = title ?: "") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }

