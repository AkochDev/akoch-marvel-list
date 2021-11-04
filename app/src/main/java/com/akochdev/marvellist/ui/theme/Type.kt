package com.akochdev.marvellist.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.akochdev.marvellist.R

// Set of Material typography styles to start with
val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_regular, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val typography = Typography(
    h6 = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    body1 = TextStyle(
        fontFamily = robotoFamily,
        color = Color.White
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
