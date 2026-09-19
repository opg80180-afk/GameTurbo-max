package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.GameTurboViewModel
import com.example.ui.components.AddGameDialog
import com.example.ui.components.BgmMusicBar
import com.example.ui.components.GameProfileSheet
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurboCrimson
import com.example.ui.theme.TurboCyan
import com.example.ui.theme.TurboGreen
import com.example.ui.theme.VoidBackground

private data class NavItem(
  val title: String,
  val activeIcon: ImageVector,
  val inactiveIcon: ImageVector,
  val tag: String
)

@Composable
fun GameTurboMainScreen(
  viewModel: GameTurboViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val navItems = listOf(
    NavItem("Space", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports, "nav_space"),
    NavItem("Booster", Icons.Filled.Speed, Icons.Outlined.Speed, "nav_booster"),
    NavItem("Network", Icons.Filled.Language, Icons.Outlined.Language, "nav_network"),
    NavItem("HUD", Icons.Filled.Widgets, Icons.Outlined.Widgets, "nav_hud")
  )

  Row(
    modifier = modifier
      .fillMaxSize()
      .background(VoidBackground)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    // Left-Docked Gaming Console Navigation Rail
    NavigationRail(
      containerColor = SurfaceDark,
      modifier = Modifier
        .fillMaxHeight()
        .width(76.dp)
        .border(
          width = 1.dp,
          color = SurfaceBorder,
          shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
        ),
      header = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(TurboCyan.copy(alpha = 0.15f))
              .border(1.5.dp, TurboCyan, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = "Game Turbo",
              tint = TurboCyan,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "TURBO",
            color = TurboCyan,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
        }
      }
    ) {
      Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        navItems.forEachIndexed { index, item ->
          val isSelected = uiState.currentTab == index
          NavigationRailItem(
            selected = isSelected,
            onClick = { viewModel.selectTab(index) },
            icon = {
              Icon(
                imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                contentDescription = item.title,
                tint = if (isSelected) TurboCyan else TextMuted,
                modifier = Modifier.size(22.dp)
              )
            },
            label = {
              Text(
                text = item.title,
                fontSize = 9.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                color = if (isSelected) TurboCyan else TextMuted
              )
            },
            colors = NavigationRailItemDefaults.colors(
              indicatorColor = TurboCyan.copy(alpha = 0.15f)
            ),
            modifier = Modifier
              .testTag(item.tag)
              .padding(vertical = 4.dp)
          )
        }
      }
    }

    // Right Widescreen Horizontal Console Area
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Top Compact Background Music ("Baila Lento" Phonk) Player Bar
        BgmMusicBar(
          isPlaying = uiState.isBgmPlaying,
          visualizerBars = uiState.bgmVisualizerBars,
          volume = uiState.bgmVolume,
          onTogglePlayPause = { viewModel.toggleBgm() },
          onVolumeChange = { viewModel.setBgmVolume(it) },
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
        )

        // Main Horizontal Content based on Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
          when (uiState.currentTab) {
            0 -> GameSpaceScreen(
              telemetry = uiState.telemetry,
              games = uiState.games,
              isBoosting = uiState.isBoosting,
              onBoostClick = { viewModel.triggerTurboBoost() },
              onLaunchGame = { viewModel.launchGame(it) },
              onTuneGame = { viewModel.openGameProfile(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) },
              onAddGameClick = { viewModel.openAddGameDialog() }
            )
            1 -> PerformanceTunerScreen(
              telemetry = uiState.telemetry,
              isBoosting = uiState.isBoosting,
              lastBoostResult = uiState.lastBoostResult,
              onBoostClick = { viewModel.triggerTurboBoost() }
            )
            2 -> NetworkAcceleratorScreen(
              telemetry = uiState.telemetry,
              regionalPings = uiState.regionalPings,
              isTestingNetwork = uiState.isTestingNetwork,
              selectedDns = uiState.selectedDns,
              dnsPresets = viewModel.dnsPresets,
              onRetestClick = { viewModel.runNetworkTest() },
              onSelectDns = { viewModel.selectDns(it) }
            )
            3 -> InGameHudScreen(
              telemetry = uiState.telemetry,
              overlayDndEnabled = uiState.overlayDndEnabled,
              overlayBrightnessLocked = uiState.overlayBrightnessLocked,
              overlayBrightnessLevel = uiState.overlayBrightnessLevel,
              overlayFpsOverlayActive = uiState.overlayFpsOverlayActive,
              overlaySelectedVoice = uiState.overlaySelectedVoice,
              overlayVoiceActive = uiState.overlayVoiceActive,
              onToggleDnd = { viewModel.toggleOverlayDnd() },
              onToggleBrightnessLock = { viewModel.toggleOverlayBrightnessLock() },
              onBrightnessChange = { viewModel.setOverlayBrightness(it) },
              onToggleFps = { viewModel.toggleOverlayFps() },
              onSelectVoice = { viewModel.selectVoiceChanger(it) },
              onQuickCleanMemory = { viewModel.triggerTurboBoost() }
            )
          }
        }
      }

      // Notification Banner Overlay
      androidx.compose.animation.AnimatedVisibility(
        visible = uiState.bannerNotification != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 10.dp, start = 20.dp, end = 20.dp)
      ) {
        uiState.bannerNotification?.let { msg ->
          Box(
            modifier = Modifier
              .fillMaxWidth(0.85f)
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceElevated)
              .border(1.5.dp, TurboCyan, RoundedCornerShape(12.dp))
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = TurboCyan,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = msg,
                  color = TextPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
              IconButton(
                onClick = { viewModel.dismissNotification() },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Dismiss",
                  tint = TextMuted,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }

      // Per-Game Profile BottomSheet
      uiState.selectedGameForProfile?.let { game ->
        GameProfileSheet(
          game = game,
          onDismiss = { viewModel.closeGameProfile() },
          onSave = { viewModel.saveGameProfile(it) },
          onDelete = { viewModel.deleteGame(it) }
        )
      }

      // Add Game Dialog
      if (uiState.isAddGameDialogOpen) {
        AddGameDialog(
          installedApps = uiState.installedApps,
          onDismiss = { viewModel.closeAddGameDialog() },
          onAddInstalled = { viewModel.addInstalledAppAsGame(it) },
          onAddCustom = { title, cat -> viewModel.addCustomGame(title, cat) }
        )
      }
    }
  }
}
