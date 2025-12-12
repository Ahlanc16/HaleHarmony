package com.example.haleharmony

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.haleharmony.ui.screens.HaleHarmonyApp
import com.example.haleharmony.ui.theme.HaleHarmonyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HaleHarmonyTheme {
                HaleHarmonyApp()
            }
        }
    }
}
