package com.akochdev.marvellist.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.akochdev.marvellist.ui.theme.GeneralBackgroundColor

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(GeneralBackgroundColor)
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}
