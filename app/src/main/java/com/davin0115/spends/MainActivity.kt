package com.davin0115.spends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.davin0115.spends.navigation.SetUpNavGraph
import com.davin0115.spends.ui.theme.SpendsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendsTheme {
                SetUpNavGraph()
            }
        }
    }
}