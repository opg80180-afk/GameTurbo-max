package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GameTurboColorScheme = darkColorScheme(
  primary = md_theme_dark_primary,
  onPrimary = md_theme_dark_onPrimary,
  primaryContainer = md_theme_dark_primaryContainer,
  onPrimaryContainer = md_theme_dark_onPrimaryContainer,
  secondary = md_theme_dark_secondary,
  onSecondary = md_theme_dark_onSecondary,
  secondaryContainer = md_theme_dark_secondaryContainer,
  onSecondaryContainer = md_theme_dark_onSecondaryContainer,
  tertiary = md_theme_dark_tertiary,
  onTertiary = md_theme_dark_onTertiary,
  tertiaryContainer = md_theme_dark_tertiaryContainer,
  onTertiaryContainer = md_theme_dark_onTertiaryContainer,
  background = md_theme_dark_background,
  onBackground = md_theme_dark_onBackground,
  surface = md_theme_dark_surface,
  onSurface = md_theme_dark_onSurface,
  surfaceVariant = md_theme_dark_surfaceVariant,
  onSurfaceVariant = md_theme_dark_onSurfaceVariant,
  outline = md_theme_dark_outline,
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = VoidBackground.toArgb()
        window.navigationBarColor = VoidBackground.toArgb()
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = GameTurboColorScheme,
    typography = Typography,
    content = content,
  )
}
