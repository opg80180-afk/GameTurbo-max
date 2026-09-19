package com.example.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.turbo.TurboLevel
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TurboAmber
import com.example.ui.theme.TurboCrimson
import com.example.ui.theme.TurboCyan

@Composable
fun TurboBoostButton(
  isBoosting: Boolean,
  currentLevel: TurboLevel,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "turbo_button_trans")

  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutLinearInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isBoosting) 800 else 8000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation_ring"
  )

  val glowAlpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.8f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutLinearInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glow_alpha"
  )

  val activeColor = if (isBoosting) TurboCrimson else TurboCyan
  val secondaryColor = if (isBoosting) TurboAmber else TurboCrimson

  Box(
    modifier = modifier
      .size(220.dp)
      .testTag("turbo_boost_button"),
    contentAlignment = Alignment.Center
  ) {
    // Outer energy glow aura
    Box(
      modifier = Modifier
        .size(216.dp)
        .scale(if (isBoosting) pulseScale * 1.08f else pulseScale)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(
              activeColor.copy(alpha = if (isBoosting) 0.45f else 0.18f * glowAlpha),
              Color.Transparent
            )
          )
        )
    )

    // Rotating dashed tech ring
    Canvas(
      modifier = Modifier
        .size(200.dp)
        .rotate(rotationAngle)
    ) {
      val strokeWidth = 2.5.dp.toPx()
      drawCircle(
        color = activeColor.copy(alpha = if (isBoosting) 0.9f else 0.4f),
        style = Stroke(
          width = strokeWidth,
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 15f, 10f, 15f), 0f)
        )
      )
    }

    // Counter-rotating outer ticks ring
    Canvas(
      modifier = Modifier
        .size(178.dp)
        .rotate(-rotationAngle * 0.7f)
    ) {
      val strokeWidth = 1.8.dp.toPx()
      drawCircle(
        color = secondaryColor.copy(alpha = 0.35f),
        style = Stroke(
          width = strokeWidth,
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 25f), 0f)
        )
      )
    }

    // Inner Button Reactor Core
    Box(
      modifier = Modifier
        .size(154.dp)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(
              SurfaceElevated,
              SurfaceDark,
              Color(0xFF070B14)
            )
          )
        )
        .border(
          width = 2.dp,
          brush = Brush.linearGradient(
            colors = listOf(activeColor, secondaryColor, activeColor)
          ),
          shape = CircleShape
        )
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true, color = activeColor),
          enabled = !isBoosting,
          onClick = onClick
        ),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
      ) {
        Icon(
          imageVector = if (isBoosting) Icons.Default.RocketLaunch else Icons.Default.Speed,
          contentDescription = "Turbo Action",
          tint = activeColor,
          modifier = Modifier
            .size(34.dp)
            .scale(if (isBoosting) pulseScale else 1f)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = if (isBoosting) "BOOSTING" else "TURBO",
          color = TextPrimary,
          fontSize = 17.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 2.sp
        )

        Text(
          text = if (isBoosting) "OPTIMIZING..." else "TAP TO BOOST",
          color = activeColor,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.sp
        )
      }
    }
  }
}
