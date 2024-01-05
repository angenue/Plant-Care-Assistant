package com.example.plantcare.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.plantcare.data.model.Plant
import com.example.plantcare.ui.viewmodel.OperationStatus
import com.example.plantcare.ui.viewmodel.PlantViewModel
import java.io.ByteArrayOutputStream
import android.util.Base64


@Composable
fun CameraScreen(viewModel: PlantViewModel, onNavigateToPlantDetails: (String) -> Unit) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val base64Image = uriToBase64(it, context)
                viewModel.identifyPlantFromImage(base64Image)
            }
        }
    )

    val plantDetailsResponse by viewModel.plantDetailsResponse.observeAsState()

    plantDetailsResponse?.let { status ->
        when (status) {
            is OperationStatus.Success -> {
                onNavigateToPlantDetails(status.data.id)
            }
            is OperationStatus.Error -> {
                Toast.makeText(context, "Failed to identify plant: ${status.exception.message}", Toast.LENGTH_SHORT).show()
                println("Failed to identify plant: ${status.exception.message}")
            }
        }
    }

    // Launch the image picker when the screen is composed
    LaunchedEffect(true) {
        imagePickerLauncher.launch("image/*")
    }
}

// Function to convert URI to base64
fun uriToBase64(uri: Uri, context: Context): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

    // Remove newline characters from the base64 string
    return "data:image/jpeg;base64," + base64String.replace("\n", "").replace("\r", "")
}

