package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun BgmMusicBar(
  isPlaying: Boolean,
  visualizerBars: List<Float>,
  volume: Float,
  onTogglePlayPause: () -> Unit,
  onVolumeChange: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  var showVolumeSlider by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "disc_rotation")
  val discAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isPlaying) 2500 else 1000000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "disc_angle"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.horizontalGradient(
          listOf(
            Color(0xFF0D1424),
            Color(0xFF131D33),
            Color(0xFF0D1424)
          )
        )
      )
      .border(
        width = 1.2.dp,
        brush = Brush.horizontalGradient(
          listOf(
            if (isPlaying) TurboCrimson else SurfaceBorder,
            if (isPlaying) TurboCyan else SurfaceBorder
          )
        ),
        shape = RoundedCornerShape(16.dp)
      )
      .padding(horizontal = 14.dp, vertical = 10.dp)
      .testTag("bgm_music_bar")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Vinyl / Disc Icon & Title
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          // Spinning Vinyl Disc
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFF070B14))
              .border(
                width = 1.5.dp,
                color = if (isPlaying) TurboCrimson else SurfaceBorder,
                shape = CircleShape
              )
              .rotate(if (isPlaying) discAngle else 0f),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.MusicNote,
              contentDescription = null,
              tint = if (isPlaying) TurboCyan else TextMuted,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Baila Lento",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (isPlaying) TurboCrimson.copy(alpha = 0.2f) else SurfaceElevated)
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "PHONK BGM",
                  color = if (isPlaying) TurboCrimson else TextMuted,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
            Text(
              text = if (isPlaying) "130 BPM • Brazilian Drift Phonk 808" else "Background music paused",
              color = if (isPlaying) TurboCyan else TextMuted,
              fontSize = 10.sp
            )
          }
        }

        // Animated Equalizer Visualizer Bars
        Row(
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.spacedBy(3.dp),
          modifier = Modifier
            .height(24.dp)
            .padding(horizontal = 8.dp)
        ) {
          val bars = if (visualizerBars.size >= 4) visualizerBars else listOf(0.3f, 0.5f, 0.7f, 0.4f)
          bars.take(4).forEachIndexed { i, barVal ->
            val animatedHeight by animateFloatAsState(
              targetValue = if (isPlaying) (barVal * 20f).coerceIn(4f, 22f) else 3f,
              animationSpec = tween(120),
              label = "bar_$i"
            )
            val barColor = when (i % 3) {
              0 -> TurboCyan
              1 -> TurboCrimson
              else -> TurboAmber
            }
            Box(
              modifier = Modifier
                .width(3.5.dp)
                .height(animatedHeight.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(barColor)
            )
          }
        }

        // Volume Toggle Button
        IconButton(
          onClick = { showVolumeSlider = !showVolumeSlider },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = when {
              volume <= 0.05f -> Icons.Default.VolumeMute
              volume < 0.5f -> Icons.Default.VolumeDown
              else -> Icons.Default.VolumeUp
            },
            contentDescription = "Volume",
            tint = if (showVolumeSlider) TurboCyan else TextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Play / Pause Circle Button
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
              if (isPlaying) TurboCrimson else TurboCyan
            )
            .clickable(onClick = onTogglePlayPause),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color(0xFF070B14),
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Expandable Volume Slider
      if (showVolumeSlider) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "VOL: ${(volume * 100).toInt()}%",
            color = TextSecondary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(62.dp)
          )
          Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..1f,
            modifier = Modifier.weight(1f),
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
