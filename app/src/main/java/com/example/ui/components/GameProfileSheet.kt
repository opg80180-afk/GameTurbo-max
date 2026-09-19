package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameProfileSheet(
  game: GameItem,
  onDismiss: () -> Unit,
  onSave: (GameItem) -> Unit,
  onDelete: (GameItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var mode by remember { mutableStateOf(game.performanceMode) }
  var targetFps by remember { mutableIntStateOf(game.targetFps) }
  var touchHz by remember { mutableIntStateOf(game.touchHz) }
  var visualEngine by remember { mutableStateOf(game.visualEnhancement) }
  var blockNotif by remember { mutableStateOf(game.blockNotifications) }
  var rejectCalls by remember { mutableStateOf(game.rejectCalls) }
  var lockBright by remember { mutableStateOf(game.lockBrightness) }
  var haptic by remember { mutableStateOf(game.hapticFeedback) }
  var netPriority by remember { mutableStateOf(game.networkPriority) }

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = SurfaceDark,
    dragHandle = null
  ) {
    Column(
      modifier = modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = TurboCyan,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Turbo Tuning",
              color = TextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = game.title,
              color = TextSecondary,
              fontSize = 13.sp
            )
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Performance Mode Selector
      SectionHeader(title = "PERFORMANCE GOVERNOR")
      Spacer(modifier = Modifier.height(8.dp))
      val modes = listOf(
        "TURBO_EXTREME" to "Turbo Extreme (Max CPU/GPU)",
        "PERFORMANCE" to "Performance (High FPS)",
        "BALANCED" to "Balanced (Thermal Safe)",
        "BATTERY_SAVER" to "Battery Saver (Max Playtime)"
      )
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        modes.forEach { (mKey, mDesc) ->
          val isSelected = mode == mKey
          val activeColor = when (mKey) {
            "TURBO_EXTREME" -> TurboCrimson
            "PERFORMANCE" -> TurboAmber
            "BATTERY_SAVER" -> TurboGreen
            else -> TurboCyan
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) activeColor.copy(alpha = 0.15f) else SurfaceElevated)
              .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) activeColor else SurfaceBorder,
                shape = RoundedCornerShape(12.dp)
              )
              .clickable { mode = mKey }
              .padding(horizontal = 14.dp, vertical = 12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = mKey.replace("_", " "),
                  color = if (isSelected) activeColor else TextPrimary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                )
                Text(
                  text = mDesc,
                  color = TextMuted,
                  fontSize = 11.sp
                )
              }
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Done,
                  contentDescription = null,
                  tint = activeColor,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Target Refresh Rate (FPS)
      SectionHeader(title = "TARGET DISPLAY REFRESH RATE")
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(60, 90, 120, 144).forEach { fps ->
          val isSelected = targetFps == fps
          FilterChip(
            selected = isSelected,
            onClick = { targetFps = fps },
            label = { Text("$fps FPS") },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TurboCyan.copy(alpha = 0.2f),
              selectedLabelColor = TurboCyan,
              containerColor = SurfaceElevated,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = SurfaceBorder,
              selectedBorderColor = TurboCyan
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Touch Sampling Rate
      SectionHeader(title = "TOUCH SAMPLING RATE")
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(120 to "Normal", 240 to "Ultra", 480 to "Extreme").forEach { (hz, title) ->
          val isSelected = touchHz == hz
          FilterChip(
            selected = isSelected,
            onClick = { touchHz = hz },
            label = { Text("${hz}Hz ($title)") },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TurboCrimson.copy(alpha = 0.2f),
              selectedLabelColor = TurboCrimson,
              containerColor = SurfaceElevated,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = SurfaceBorder,
              selectedBorderColor = TurboCrimson
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Gaming DND & Telemetry Controls
      SectionHeader(title = "DO NOT DISTURB & GAMING ENGINE")
      Spacer(modifier = Modifier.height(8.dp))
      Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
      ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
          SettingToggleRow(
            title = "Block Floating Notifications",
            subtitle = "Prevent pop-up banners while in-game",
            checked = blockNotif,
            onCheckedChange = { blockNotif = it }
          )
          SettingToggleRow(
            title = "Reject Calls / Silent Alerts",
            subtitle = "Route calls directly to voicemail without interrupt",
            checked = rejectCalls,
            onCheckedChange = { rejectCalls = it }
          )
          SettingToggleRow(
            title = "Lock Screen Brightness",
            subtitle = "Prevent auto-dimming when fingers cover sensor",
            checked = lockBright,
            onCheckedChange = { lockBright = it }
          )
          SettingToggleRow(
            title = "4D Gaming Shock (Tactile Haptic)",
            subtitle = "Tactile motor feedback on critical actions",
            checked = haptic,
            onCheckedChange = { haptic = it }
          )
          SettingToggleRow(
            title = "Network Packet Prioritization",
            subtitle = "Bypass background data to ensure lowest ping",
            checked = netPriority,
            onCheckedChange = { netPriority = it },
            isLast = true
          )
        }
      }

      Spacer(modifier = Modifier.height(22.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = { onDelete(game) },
          modifier = Modifier
            .weight(0.35f)
            .height(48.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TurboCrimson),
          border = androidx.compose.foundation.BorderStroke(1.dp, TurboCrimson.copy(alpha = 0.5f))
        ) {
          Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Remove",
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "REMOVE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            val updated = game.copy(
              performanceMode = mode,
              targetFps = targetFps,
              touchHz = touchHz,
              visualEnhancement = visualEngine,
              blockNotifications = blockNotif,
              rejectCalls = rejectCalls,
              lockBrightness = lockBright,
              hapticFeedback = haptic,
              networkPriority = netPriority
            )
            onSave(updated)
          },
          modifier = Modifier
            .weight(0.65f)
            .height(48.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = TurboCyan,
            contentColor = Color(0xFF070B14)
          )
        ) {
          Icon(
            imageVector = Icons.Default.Speed,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "APPLY TURBO PROFILE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}

@Composable
private fun SectionHeader(title: String) {
  Text(
    text = title,
    color = TextSecondary,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily.Monospace,
    letterSpacing = 1.sp
  )
}

@Composable
private fun SettingToggleRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  isLast: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 11.sp
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
}
