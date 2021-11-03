package com.akochdev.marvellist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.akochdev.marvellist.ui.navigation.AppNavigation
import com.akochdev.marvellist.ui.theme.MarvelListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelListTheme {
                AppNavigation()
            }
        }
    }
}
