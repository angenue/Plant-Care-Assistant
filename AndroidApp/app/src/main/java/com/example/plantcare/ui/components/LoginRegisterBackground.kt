package com.example.plantcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.DarkGray
import com.example.plantcare.ui.theme.LightGray

class LoginRegisterBackground {
    @Preview
    @Composable
    fun MyApp() {
        MaterialTheme {
            // Box used for the gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                DarkGray // Light gray color at the bottom
                            ),

                        )
                    )
            ) { Column(
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
                modifier = Modifier.width(600.dp)
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