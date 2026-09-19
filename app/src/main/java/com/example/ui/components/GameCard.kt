package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameItem
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
fun GameCard(
  game: GameItem,
  onLaunch: () -> Unit,
  onTune: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val resId = rememberDrawableResId(context, game.bannerDrawableName)

  val modeColor = when (game.performanceMode) {
    "TURBO_EXTREME" -> TurboCrimson
    "PERFORMANCE" -> TurboAmber
    "BATTERY_SAVER" -> TurboGreen
    else -> TurboCyan
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(SurfaceDark)
      .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
      .testTag("game_card_${game.packageName}")
  ) {
    Column {
      // Banner Image with Overlays
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
      ) {
        if (resId != 0) {
          Image(
            painter = painterResource(id = resId),
            contentDescription = game.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        } else {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.linearGradient(
                  listOf(SurfaceElevated, Color(0xFF1E293B))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.SportsEsports,
              contentDescription = null,
              tint = TurboCyan.copy(alpha = 0.4f),
              modifier = Modifier.size(56.dp)
            )
          }
        }

        // Gradient overlay for smooth transition to content
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Black.copy(alpha = 0.25f),
                  Color.Black.copy(alpha = 0.4f),
                  SurfaceDark
                )
              )
            )
        )

        // Top Row: Performance Mode Tag & Favorite Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(modeColor.copy(alpha = 0.25f))
              .border(1.dp, modeColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = modeColor,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = game.performanceMode.replace("_", " "),
                color = modeColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.6f))
          ) {
            Icon(
              imageVector = if (game.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
              contentDescription = "Favorite",
              tint = if (game.isFavorite) TurboAmber else TextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Details and Actions
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = game.title,
              color = TextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = "${game.category} • ${game.playTimeMinutes}m played",
              color = TextMuted,
              fontSize = 12.sp
            )
          }

          // Target FPS Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(SurfaceElevated)
              .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "${game.targetFps} FPS",
              color = TurboCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Specs badges: Touch Rate & DND
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SpecPill(label = "Touch: ${game.touchHz}Hz")
          if (game.blockNotifications) {
            SpecPill(label = "DND Active")
          }
          if (game.networkPriority) {
            SpecPill(label = "Net Prioritized")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onTune,
            modifier = Modifier
              .weight(0.42f)
              .height(42.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Tune",
              tint = TextSecondary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "TUNE",
              color = TextSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Button(
            onClick = onLaunch,
            modifier = Modifier
              .weight(0.58f)
              .height(42.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = TurboCyan,
              contentColor = Color(0xFF070B14)
            )
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Launch",
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LAUNCH TURBO",
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SpecPill(label: String) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(Color(0xFF162032))
      .padding(horizontal = 7.dp, vertical = 3.dp)
  ) {
    Text(
      text = label,
      color = TextSecondary,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
fun rememberDrawableResId(context: android.content.Context, name: String?): Int {
  if (name.isNullOrBlank()) return 0
  return try {
    context.resources.getIdentifier(name, "drawable", context.packageName)
  } catch (e: Exception) {
    0
  }
}
