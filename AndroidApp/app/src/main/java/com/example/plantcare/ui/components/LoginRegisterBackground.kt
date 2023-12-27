package com.example.plantcare.ui.components

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.DarkGray
import com.example.plantcare.ui.theme.LightGray

class LoginRegisterBackground {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun MyApp() {
        MaterialTheme {
            Scaffold(
                topBar = { CustomTopBar() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    DarkGray // Color at the bottom
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TopSection()
                        MiddleSection()
                        BottomSection()
                    }
                }
            }
        }
    }

    /*@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomTopBar() {
        TopAppBar(
            title = {
                // Title content
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black,
            )
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "App Name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp) // Adjust padding as needed
                )
                Image(
                    painter = painterResource(id = R.drawable.water_drop),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp) // Adjust size as needed
                )
            }
        }
    }*/

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomTopBar(){
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
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    // App Icon in the center
                    Image(
                        painter = painterResource(id = R.drawable.water_drop),
                        contentDescription = "App Icon",
                        modifier = Modifier.weight(1f)
                            .size(34.dp)
                    )

                    // Spacer to balance the layout
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
    }





    @Preview
    @Composable
    fun TopSectionPreview() {
        TopSection()
    }

    @Composable
    fun TopSection() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.topplant),
                contentDescription = "Top Image",
                modifier = Modifier
                    .width(600.dp)
                    .height(410.dp)
            )
        }
    }

    @Composable
    fun MiddleSection() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .height(300.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
            shape = RoundedCornerShape(28.dp)
        ) {
            // login or register
        }
    }


    @Preview
    @Composable
    fun BottomSectionPreview() {
        BottomSection()
    }

    @Composable
    fun BottomSection() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.bottomleaves),
                contentDescription = "Bottom Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}