package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.turbo.SystemTelemetry
import com.example.turbo.TurboBoostResult
import com.example.ui.components.TelemetryGaugesGrid
import com.example.ui.components.TurboBoostButton
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurboAmber
import com.example.ui.theme.TurboCrimson
import com.example.ui.theme.TurboCyan
import com.example.ui.theme.TurboGreen
import com.example.ui.theme.VoidBackground

@Composable
fun PerformanceTunerScreen(
  telemetry: SystemTelemetry,
  isBoosting: Boolean,
  lastBoostResult: TurboBoostResult?,
  onBoostClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Horizontal 2-Pane Split for Landscape Handheld
  Row(
    modifier = modifier
      .fillMaxSize()
      .background(VoidBackground)
      .padding(horizontal = 14.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Left Pane: Turbo Reactor Core & Acceleration Status
    Column(
      modifier = Modifier
        .weight(0.44f)
        .fillMaxHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = "CORE GOVERNOR",
          color = TurboCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.2.sp
        )
        Text(
          text = "Hardware Acceleration",
          color = TextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.Center
      ) {
        TurboBoostButton(
          isBoosting = isBoosting,
          currentLevel = telemetry.activeTurboLevel,
          onClick = onBoostClick
        )
      }

      // Boost Status / Result Card
      if (lastBoostResult != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.horizontalGradient(
                listOf(
                  TurboCrimson.copy(alpha = 0.15f),
                  TurboCyan.copy(alpha = 0.15f)
                )
              )
            )
            .border(1.dp, TurboCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "⚡ FREED ${lastBoostResult.freedRamMb} MB",
                color = TurboCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = "Memory purged • Ping improved",
                color = TextSecondary,
                fontSize = 10.sp
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(TurboGreen.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
              Text(
                text = "-${lastBoostResult.pingImprovedMs}ms",
                color = TurboGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      } else {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "TURBO ENGINE READY • ALL CORES UNLOCKED",
            color = TurboGreen,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Right Pane: 4 Telemetry Gauges + Specs Detail Card
    LazyColumn(
      modifier = Modifier
        .weight(0.56f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      item {
        TelemetryGaugesGrid(telemetry = telemetry)
      }

      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "HARDWARE TELEMETRY SPECIFICATIONS",
              color = TextSecondary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            HardwareDetailRow(
              icon = Icons.Default.Memory,
              title = "CPU Core Governor",
              value = "Octa-Core @ ${telemetry.cpuFrequencyGhz} GHz (Turbo Gov)",
              statusColor = TurboCyan
            )

            HardwareDetailRow(
              icon = Icons.Default.Bolt,
              title = "System RAM Allocation",
              value = "${telemetry.ramUsedMb} MB / ${telemetry.ramTotalMb} MB (${telemetry.ramPercent}%)",
              statusColor = if (telemetry.ramPercent > 75) TurboAmber else TurboGreen
            )

            HardwareDetailRow(
              icon = Icons.Default.BatteryChargingFull,
              title = "Battery & Thermal State",
              value = "${telemetry.batteryLevelPercent}% • ${telemetry.batteryTempCelsius}°C ${if (telemetry.isCharging) "(Charging)" else ""}",
              statusColor = if (telemetry.batteryTempCelsius > 40) TurboCrimson else TurboGreen
            )

            HardwareDetailRow(
              icon = Icons.Default.Wifi,
              title = "Network Transport",
              value = "${telemetry.networkType} (Jitter ${telemetry.networkJitterMs}ms)",
              statusColor = TurboCyan,
              isLast = true
            )
          }
        }
      }
    }
  }
}

@Composable
private fun HardwareDetailRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String,
  statusColor: Color,
  isLast: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(SurfaceElevated),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = statusColor,
        modifier = Modifier.size(15.dp)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = TextSecondary,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = value,
        color = TextPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace
      )
    }
  }

  if (!isLast) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(SurfaceBorder.copy(alpha = 0.5f))
    )
  }
}
