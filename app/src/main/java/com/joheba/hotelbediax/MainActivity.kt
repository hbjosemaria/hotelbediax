package com.joheba.hotelbediax

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.joheba.hotelbediax.ui.navigation.AppNavigation
import com.joheba.hotelbediax.ui.theme.HotelBediaXTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HotelBediaXTheme {
                AppNavigation()
            }
        }
    }
}