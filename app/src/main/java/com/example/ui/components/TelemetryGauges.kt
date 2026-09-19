package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun CircularHudGauge(
  value: Float,
  maxValue: Float = 100f,
  label: String,
  unit: String,
  color: Color,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  val progress = (value / maxValue).coerceIn(0f, 1f)
  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    label = "gauge_anim"
  )

  Column(
    modifier = modifier
      .background(SurfaceDark, RoundedCornerShape(16.dp))
      .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier.size(76.dp),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(76.dp)) {
        val strokeWidth = 7.dp.toPx()
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
        val arcSize = Size(diameter, diameter)

        // Background Track
        drawArc(
          color = Color(0xFF1B2438),
          startAngle = 135f,
          sweepAngle = 270f,
          useCenter = false,
          topLeft = topLeft,
          size = arcSize,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Active Arc
        drawArc(
          brush = Brush.sweepGradient(
            0.0f to color.copy(alpha = 0.6f),
            1.0f to color,
            center = Offset(size.width / 2, size.height / 2)
          ),
          startAngle = 135f,
          sweepAngle = 270f * animatedProgress,
          useCenter = false,
          topLeft = topLeft,
          size = arcSize,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = if (value.isFinite()) value.toInt().toString() else "0",
          color = TextPrimary,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = label,
      color = TextSecondary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )

    Text(
      text = unit,
      color = TextMuted,
      fontSize = 10.sp,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
fun TelemetryGaugesGrid(
  telemetry: SystemTelemetry,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      CircularHudGauge(
        value = telemetry.cpuUsagePercent.toFloat(),
        maxValue = 100f,
        label = "CPU LOAD",
        unit = "${telemetry.cpuFrequencyGhz} GHz",
        color = if (telemetry.cpuUsagePercent > 65) TurboCrimson else TurboCyan,
        icon = Icons.Default.Memory,
        modifier = Modifier.weight(1f)
      )

      CircularHudGauge(
        value = telemetry.ramPercent.toFloat(),
        maxValue = 100f,
        label = "RAM USAGE",
        unit = "${telemetry.ramUsedMb}/${telemetry.ramTotalMb}M",
        color = if (telemetry.ramPercent > 75) TurboAmber else TurboCyan,
        icon = Icons.Default.ElectricBolt,
        modifier = Modifier.weight(1f)
      )

      CircularHudGauge(
        value = telemetry.networkPingMs.toFloat(),
        maxValue = 120f,
        label = "LATENCY",
        unit = "${telemetry.networkPingMs} ms",
        color = if (telemetry.networkPingMs < 35) TurboGreen else TurboAmber,
        icon = Icons.Default.NetworkCheck,
        modifier = Modifier.weight(1f)
      )

      CircularHudGauge(
        value = telemetry.batteryTempCelsius,
        maxValue = 55f,
        label = "TEMP",
        unit = "${telemetry.batteryTempCelsius.toInt()}°C",
        color = if (telemetry.batteryTempCelsius > 42) TurboCrimson else TurboGreen,
        icon = Icons.Default.Thermostat,
        modifier = Modifier.weight(1f)
      )
    }
  }
}
