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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.GameItem
import com.example.turbo.SystemTelemetry
import com.example.ui.components.GameCard
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
fun GameSpaceScreen(
  telemetry: SystemTelemetry,
  games: List<GameItem>,
  isBoosting: Boolean,
  onBoostClick: () -> Unit,
  onLaunchGame: (GameItem) -> Unit,
  onTuneGame: (GameItem) -> Unit,
  onToggleFavorite: (GameItem) -> Unit,
  onAddGameClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(VoidBackground)
  ) {
    // Horizontal 2-Column Grid for Widescreen Landscape
    LazyVerticalGrid(
      columns = GridCells.Adaptive(minSize = 320.dp),
      modifier = Modifier
        .fillMaxSize()
        .testTag("game_space_list"),
      contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Sleek Horizontal Telemetry Banner (Spans full width)
      item(span = { GridItemSpan(maxLineSpan) }) {
        TurboHeaderBanner(
          telemetry = telemetry,
          isBoosting = isBoosting,
          onBoostClick = onBoostClick
        )
      }

      // Section Header (Spans full width)
      item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.SportsEsports,
              contentDescription = null,
              tint = TurboCyan,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "GAME LIBRARY",
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(SurfaceElevated)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${games.size} TITLES",
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Text(
            text = "+ Add Game",
            color = TurboCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onAddGameClick)
          )
        }
      }

      // Games Cards (2-column layout in landscape)
      if (games.isEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          EmptyGamesState(onAddGameClick = onAddGameClick)
        }
      } else {
        items(games, key = { it.packageName }) { game ->
          GameCard(
            game = game,
            onLaunch = { onLaunchGame(game) },
            onTune = { onTuneGame(game) },
            onToggleFavorite = { onToggleFavorite(game) }
          )
        }
      }
    }

    // Floating Action Button
    FloatingActionButton(
      onClick = onAddGameClick,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 20.dp, bottom = 16.dp)
        .testTag("fab_add_game"),
      containerColor = TurboCyan,
      contentColor = Color(0xFF070B14)
    ) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Game")
    }
  }
}

@Composable
private fun TurboHeaderBanner(
  telemetry: SystemTelemetry,
  isBoosting: Boolean,
  onBoostClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(SurfaceDark)
      .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
  ) {
    // Banner Background Art
    Image(
      painter = painterResource(id = R.drawable.img_turbo_hero),
      contentDescription = null,
      modifier = Modifier
        .fillMaxWidth()
        .height(96.dp),
      contentScale = ContentScale.Crop
    )

    // Dark Gradient Overlay for text contrast
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(96.dp)
        .background(
          Brush.horizontalGradient(
            listOf(
              Color(0xFF0A0F1D).copy(alpha = 0.95f),
              Color(0xFF0A0F1D).copy(alpha = 0.85f),
              Color.Black.copy(alpha = 0.5f)
            )
          )
        )
    )

    // Foreground Content in Horizontal Flow
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Brand and Mode
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (isBoosting) TurboCrimson else TurboGreen)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isBoosting) "ACCELERATING SYSTEM..." else "TURBO EXTREME ACTIVE",
            color = if (isBoosting) TurboCrimson else TurboGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Game Turbo 5.0",
          color = TextPrimary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
      }

      // Middle: Real-time 4 metrics
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF0A0E18).copy(alpha = 0.85f))
          .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
          .padding(vertical = 6.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        TelemetryMiniStat(
          icon = Icons.Default.Memory,
          label = "CPU",
          value = "${telemetry.cpuUsagePercent}%",
          color = if (telemetry.cpuUsagePercent > 65) TurboCrimson else TurboCyan
        )
        TelemetryMiniStat(
          icon = Icons.Default.ElectricBolt,
          label = "RAM",
          value = "${telemetry.ramPercent}%",
          color = if (telemetry.ramPercent > 75) TurboAmber else TurboCyan
        )
        TelemetryMiniStat(
          icon = Icons.Default.NetworkCheck,
          label = "PING",
          value = "${telemetry.networkPingMs}ms",
          color = if (telemetry.networkPingMs < 40) TurboGreen else TurboAmber
        )
        TelemetryMiniStat(
          icon = Icons.Default.Speed,
          label = "FPS",
          value = "${telemetry.targetFps}",
          color = TurboCyan
        )
      }

      // Right: Quick Boost Button
      Button(
        onClick = onBoostClick,
        enabled = !isBoosting,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TurboCrimson,
          contentColor = Color.White
        ),
        modifier = Modifier.height(38.dp)
      ) {
        Icon(
          imageVector = Icons.Default.RocketLaunch,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (isBoosting) "BOOSTING" else "BOOST",
          fontSize = 11.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}

@Composable
private fun TelemetryMiniStat(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String,
  color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(12.dp)
      )
      Spacer(modifier = Modifier.width(2.dp))
      Text(
        text = label,
        color = TextMuted,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
      )
    }
    Text(
      text = value,
      color = TextPrimary,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
private fun EmptyGamesState(onAddGameClick: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(SurfaceDark)
      .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = Icons.Default.SportsEsports,
        contentDescription = null,
        tint = TurboCyan.copy(alpha = 0.5f),
        modifier = Modifier.size(48.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "No Games in Turbo Space",
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Add games from your device to enable high-octane FPS boost and custom touch tuning.",
        color = TextMuted,
        fontSize = 11.sp
      )
      Spacer(modifier = Modifier.height(12.dp))
      Button(
        onClick = onAddGameClick,
        colors = ButtonDefaults.buttonColors(containerColor = TurboCyan)
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("ADD GAME", color = Color(0xFF070B14), fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }
  }
}
