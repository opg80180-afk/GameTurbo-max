package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.GameTurboViewModel
import com.example.ui.screens.GameTurboMainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VoidBackground

class MainActivity : ComponentActivity() {
  private val viewModel: GameTurboViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = VoidBackground
        ) {
          GameTurboMainScreen(viewModel = viewModel)
        }
      }
    }
  }
}
