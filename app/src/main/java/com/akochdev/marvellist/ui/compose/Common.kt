package com.akochdev.marvellist.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.akochdev.marvellist.R
import com.akochdev.marvellist.ui.theme.GeneralBackgroundColor
import com.akochdev.marvellist.ui.theme.MineShaft
import com.akochdev.marvellist.ui.theme.typography

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

@Composable
fun ErrorScreen(errorMessage: String? = null, reloadAction: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GeneralBackgroundColor)
    ) {
        val (image, message, button) = createRefs()

        Image(
            modifier = Modifier
                .size(200.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                },
            painter = painterResource(id = R.drawable.ic_baseline_wifi_off_24_white),
            contentDescription = stringResource(id = R.string.error_screen_no_wifi_image)
        )
        val errorText = errorMessage ?: stringResource(id = R.string.text_general_connection_error)
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .constrainAs(message) {
                    top.linkTo(image.bottom, 20.dp)
                },
            text = errorText,
            textAlign = TextAlign.Center,
            style = typography.h6.copy(fontSize = 20.sp)
        )
        OutlinedButton(
            modifier = Modifier.constrainAs(button) {
                top.linkTo(message.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onClick = { reloadAction.invoke() },
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.White,
                contentColor = MineShaft,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text(
                text = stringResource(id = R.string.text_button_reload),
                style = typography.h6.copy(color = MineShaft)
            )
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen("There was an error while retrieving the data, please check your connection and try again") {}
}