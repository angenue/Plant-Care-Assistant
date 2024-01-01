package com.example.plantcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.LexendFontFamily

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun PreviewTopBarWithSettings() {
    TopBarWithSettings(onSettingsClick = {
        // Dummy navigation action for preview
        println("Settings icon clicked")
    })
}

    @ExperimentalMaterial3Api
    @Composable
    fun TopBarWithSettings(onSettingsClick: () -> Unit) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black,
            ),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title in the top left corner
                    Text(
                        text = "PlantPal",
                        modifier = Modifier.wrapContentWidth(Alignment.Start),
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // App Icon in the center
                    Image(
                        painter = painterResource(id = R.drawable.water_drop),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(34.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // User Icon Button
                    IconButton(onClick = onSettingsClick,
                        modifier = Modifier.padding(end = 16.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.LightGray, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person, // Use the appropriate icon
                                contentDescription = "Settings",
                                modifier = Modifier.size(24.dp) // Size of the icon inside the circle
                            )
                        }
                        }
                }
            }
        )
    }
