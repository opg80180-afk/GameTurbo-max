package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.turbo.SystemTelemetry
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurboAmber
import com.example.ui.theme.TurboCrimson
import com.example.ui.theme.TurboCyan
import com.example.ui.theme.TurboGreen
import com.example.ui.theme.VoidBackground

@Composable
fun InGameHudScreen(
  telemetry: SystemTelemetry,
  overlayDndEnabled: Boolean,
  overlayBrightnessLocked: Boolean,
  overlayBrightnessLevel: Float,
  overlayFpsOverlayActive: Boolean,
  overlaySelectedVoice: String,
  overlayVoiceActive: Boolean,
  onToggleDnd: () -> Unit,
  onToggleBrightnessLock: () -> Unit,
  onBrightnessChange: (Float) -> Unit,
  onToggleFps: () -> Unit,
  onSelectVoice: (String) -> Unit,
  onQuickCleanMemory: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showScreenshotFlash by remember { mutableStateOf(false) }

  // Horizontal 2-Pane Split for Landscape Handheld
  Row(
    modifier = modifier
      .fillMaxSize()
      .background(VoidBackground)
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .testTag("in_game_hud_screen"),
    horizontalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Left Pane: In-Game Simulation Canvas with Floating Pill & Toolbox
    Column(
      modifier = Modifier
        .weight(0.52f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(
          text = "IN-GAME OVERLAY SIMULATOR",
          color = TurboCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Floating Gaming Toolbox",
          color = TextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .clip(RoundedCornerShape(16.dp))
          .background(Color.Black)
          .border(1.5.dp, SurfaceBorder, RoundedCornerShape(16.dp))
      ) {
        // Game Scene Background
        Image(
          painter = painterResource(id = R.drawable.img_game_cyberstrike),
          contentDescription = null,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Dark Vignette
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Black.copy(alpha = 0.5f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.7f)
                )
              )
            )
        )

        // Floating Mini Telemetry HUD Pill (Top Left)
        if (overlayFpsOverlayActive) {
          Box(
            modifier = Modifier
              .padding(top = 10.dp, start = 10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF090D18).copy(alpha = 0.88f))
              .border(1.dp, TurboCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              HudMiniMetric(label = "FPS", value = "${telemetry.targetFps}", color = TurboCyan)
              Text("•", color = SurfaceBorder, fontSize = 9.sp)
              HudMiniMetric(label = "CPU", value = "${telemetry.cpuUsagePercent}%", color = if (telemetry.cpuUsagePercent > 65) TurboCrimson else TurboCyan)
              Text("•", color = SurfaceBorder, fontSize = 9.sp)
              HudMiniMetric(label = "PING", value = "${telemetry.networkPingMs}ms", color = TurboGreen)
            }
          }
        }

        // Floating Sidebar Toolbox Panel (Center Right)
        Box(
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C1322).copy(alpha = 0.92f))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp)
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            FloatingToolAction(
              icon = Icons.Default.CleaningServices,
              color = TurboCyan,
              label = "RAM",
              onClick = onQuickCleanMemory
            )
            FloatingToolAction(
              icon = Icons.Default.NotificationsOff,
              color = if (overlayDndEnabled) TurboCrimson else TextMuted,
              label = "DND",
              onClick = onToggleDnd
            )
            FloatingToolAction(
              icon = Icons.Default.CameraAlt,
              color = TurboAmber,
              label = "SNAP",
              onClick = { showScreenshotFlash = true }
            )
            FloatingToolAction(
              icon = Icons.Default.Mic,
              color = if (overlayVoiceActive) TurboGreen else TextMuted,
              label = "VOICE",
              onClick = { onSelectVoice(if (overlayVoiceActive) "Original" else "Cyborg") }
            )
          }
        }

        // Screenshot Flash Effect
        if (showScreenshotFlash) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.White.copy(alpha = 0.8f))
          )
          androidx.compose.runtime.LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(120)
            showScreenshotFlash = false
          }
        }

        // Bottom In-Game Status Label
        Box(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(10.dp)
        ) {
          Text(
            text = "🎮 IN-GAME TURBO ACTIVE [CyberStrike 2088]",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Right Pane: Toolbox Module Configuration
    LazyColumn(
      modifier = Modifier
        .weight(0.48f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      // Voice Changer Selector
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                tint = TurboCyan,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Voice Modulator (Discord/Mic)",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val voices = listOf("Original", "Cyborg", "Deep Bass", "Alien", "High Pitch")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              voices.forEach { v ->
                val isSelected = overlaySelectedVoice == v
                FilterChip(
                  selected = isSelected,
                  onClick = { onSelectVoice(v) },
                  label = { Text(v, fontSize = 9.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = TurboGreen.copy(alpha = 0.2f),
                    selectedLabelColor = TurboGreen,
                    containerColor = SurfaceElevated,
                    labelColor = TextSecondary
                  ),
                  border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = SurfaceBorder,
                    selectedBorderColor = TurboGreen
                  )
                )
              }
            }
          }
        }
      }

      // HUD Toggles & Brightness Lock
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
            HudControlToggle(
              title = "Display Floating FPS & CPU Pill",
              subtitle = "Shows real-time framerate in corner",
              checked = overlayFpsOverlayActive,
              onCheckedChange = { onToggleFps() }
            )

            HudControlToggle(
              title = "Anti-Distraction DND (Game Mode)",
              subtitle = "Silences incoming alarms & calls",
              checked = overlayDndEnabled,
              onCheckedChange = { onToggleDnd() }
            )

            HudControlToggle(
              title = "Screen Brightness Lock",
              subtitle = "Keeps display at maximum brightness",
              checked = overlayBrightnessLocked,
              onCheckedChange = { onToggleBrightnessLock() },
              isLast = !overlayBrightnessLocked
            )

            if (overlayBrightnessLocked) {
              Column(modifier = Modifier.padding(bottom = 6.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Locked Level", color = TextSecondary, fontSize = 10.sp)
                  Text("${(overlayBrightnessLevel * 100).toInt()}%", color = TurboCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                  value = overlayBrightnessLevel,
                  onValueChange = onBrightnessChange,
                  valueRange = 0.4f..1.0f,
                  colors = SliderDefaults.colors(
                    thumbColor = TurboCyan,
                    activeTrackColor = TurboCyan,
                    inactiveTrackColor = SurfaceElevated
                  )
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun HudMiniMetric(label: String, value: String, color: Color) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
      text = label,
      color = TextMuted,
      fontSize = 8.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace
    )
    Spacer(modifier = Modifier.width(2.dp))
    Text(
      text = value,
      color = color,
      fontSize = 9.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
private fun FloatingToolAction(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  label: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .clickable(onClick = onClick)
      .padding(3.dp)
  ) {
    Box(
      modifier = Modifier
        .size(26.dp)
        .clip(CircleShape)
        .background(color.copy(alpha = 0.15f))
        .border(1.dp, color.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = color,
        modifier = Modifier.size(13.dp)
      )
    }
    Spacer(modifier = Modifier.height(1.dp))
    Text(
      text = label,
      color = color,
      fontSize = 7.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
private fun HudControlToggle(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  isLast: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 9.sp
      )
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = TurboCyan,
        checkedTrackColor = TurboCyan.copy(alpha = 0.3f),
        uncheckedThumbColor = TextMuted,
        uncheckedTrackColor = SurfaceDark
      )
    )
  }
  if (!isLast) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(SurfaceBorder.copy(alpha = 0.4f))
    )
  }
}
