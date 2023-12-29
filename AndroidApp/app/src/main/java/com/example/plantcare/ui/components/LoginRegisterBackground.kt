package com.example.plantcare.ui.components

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.ArmyGreen
import com.example.plantcare.ui.theme.DarkGray
import com.example.plantcare.ui.theme.LexendFontFamily
import com.example.plantcare.ui.theme.LightGray

class LoginRegisterBackground {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        MaterialTheme {
            Scaffold(
                topBar = { CustomTopBar() }
            ) {
                BoxWithConstraints(
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
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
                    ) {
                        TopSection()
                        content()
                        BottomSection()
                    }
                }
            }
        }
    }

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
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp
                    )

                    // App Icon in the center
                    Image(
                        painter = painterResource(id = R.drawable.water_drop),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .weight(1f)
                            .size(34.dp)
                    )
                    // Spacer to balance the layout
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
    }


    @Composable
    fun TopSection() {
        BoxWithConstraints {
            val topPadding = if (maxWidth < 600.dp) 50.dp else 100.dp
            val imageHeight = if (maxWidth < 600.dp) 150.dp else 300.dp
            val imageWidth = maxWidth * if (maxWidth < 600.dp) 0.5f else 1f // Use 50% of width for smaller screens

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding)
                    .height(imageHeight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.topplant),
                    contentDescription = "Top Image",
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                )
            }
        }
    }


    @Composable
    fun MiddleSection(title: String, content: @Composable () -> Unit) {
        BoxWithConstraints { // Use BoxWithConstraints to obtain the constraints of the Box
            val cardWidthFraction = if (maxWidth < 600.dp) 0.9f else 0.75f // Smaller screens use a larger fraction

            Card(
                modifier = Modifier
                    .fillMaxWidth(cardWidthFraction) // Use a fraction of the width
                    .wrapContentHeight() // The height will be dynamic based on the content
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp,
                        color = ArmyGreen,
                        textAlign = TextAlign.Center, // Center the text horizontally
                        modifier = Modifier.fillMaxWidth() // Ensure the Text composable fills the width of the Column
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
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