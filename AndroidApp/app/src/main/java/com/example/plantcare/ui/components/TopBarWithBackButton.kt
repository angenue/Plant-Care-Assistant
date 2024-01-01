package com.example.plantcare.ui.components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantcare.R

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun PreviewTopBarWithBackButton() {
    TopBarWithBackButton(onBackClick = {
        // This is just for preview purposes, so no actual back action is needed.
        // In real use, you would hook this up to your navigation controller.
    })
}
@ExperimentalMaterial3Api
@Composable
fun TopBarWithBackButton(onBackClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black,
        ),
        navigationIcon = {
            // Back Button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spacer to push the image to the center
                Spacer(modifier = Modifier.weight(1f))

                // App Icon in the center
                Image(
                    painter = painterResource(id = R.drawable.water_drop),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(34.dp)
                )

                // Spacer to keep the image in the center
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}
