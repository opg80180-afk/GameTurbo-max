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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import com.example.data.InstalledApp
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurboCyan

@Composable
fun AddGameDialog(
  installedApps: List<InstalledApp>,
  onDismiss: () -> Unit,
  onAddInstalled: (InstalledApp) -> Unit,
  onAddCustom: (title: String, category: String) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var searchQuery by remember { mutableStateOf("") }
  var customTitle by remember { mutableStateOf("") }
  var customCategory by remember { mutableStateOf("Esports FPS") }

  val filteredApps = remember(installedApps, searchQuery) {
    if (searchQuery.isBlank()) installedApps
    else installedApps.filter { it.label.contains(searchQuery, ignoreCase = true) || it.packageName.contains(searchQuery, ignoreCase = true) }
  }

  Dialog(onDismissRequest = onDismiss) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(SurfaceDark)
        .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
        .padding(18.dp)
        .testTag("add_game_dialog")
    ) {
      Column {
        // Header
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
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Add to Game Space",
              color = TextPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = SurfaceDark,
          contentColor = TurboCyan,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = TurboCyan
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Device Apps", fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Custom Title", fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
          // Device Installed Apps List
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search installed games/apps...", fontSize = 13.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = SurfaceElevated,
              unfocusedContainerColor = SurfaceElevated,
              focusedBorderColor = TurboCyan,
              unfocusedBorderColor = SurfaceBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 280.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            if (filteredApps.isEmpty()) {
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text("No apps found", color = TextMuted, fontSize = 13.sp)
                }
              }
            } else {
              items(filteredApps, key = { it.packageName }) { app ->
                InstalledAppRow(app = app, onAdd = { onAddInstalled(app) })
              }
            }
          }
        } else {
          // Custom Game Input Tab
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Add any game title to enable Turbo tuning profile, HUD telemetry, and acceleration presets.",
              color = TextSecondary,
              fontSize = 12.sp
            )

            OutlinedTextField(
              value = customTitle,
              onValueChange = { customTitle = it },
              label = { Text("Game Title") },
              placeholder = { Text("e.g. Call of Duty: Mobile") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceElevated,
                unfocusedContainerColor = SurfaceElevated,
                focusedBorderColor = TurboCyan,
                unfocusedBorderColor = SurfaceBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
              value = customCategory,
              onValueChange = { customCategory = it },
              label = { Text("Genre / Category") },
              placeholder = { Text("e.g. Battle Royale, MOBA, Racing") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceElevated,
                unfocusedContainerColor = SurfaceElevated,
                focusedBorderColor = TurboCyan,
                unfocusedBorderColor = SurfaceBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(12.dp)
            )

            Button(
              onClick = {
                if (customTitle.isNotBlank()) {
                  onAddCustom(customTitle.trim(), customCategory.trim())
                }
              },
              enabled = customTitle.isNotBlank(),
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TurboCyan)
            ) {
              Icon(Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ADD TO GAME SPACE",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun InstalledAppRow(
  app: InstalledApp,
  onAdd: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(SurfaceElevated)
      .clickable(onClick = onAdd)
      .padding(horizontal = 10.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      if (app.icon != null) {
        val bitmap = remember(app.icon) {
          try {
            app.icon.toBitmap(96, 96).asImageBitmap()
          } catch (e: Exception) {
            null
          }
        }
        if (bitmap != null) {
          Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
          )
        } else {
          FallbackAppIcon()
        }
      } else {
        FallbackAppIcon()
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Text(
          text = app.label,
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = app.packageName,
          color = TextMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    IconButton(
      onClick = onAdd,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(TurboCyan.copy(alpha = 0.15f))
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add",
        tint = TurboCyan,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
private fun FallbackAppIcon() {
  Box(
    modifier = Modifier
      .size(36.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF1F2A3F)),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.SportsEsports,
      contentDescription = null,
      tint = TurboCyan,
      modifier = Modifier.size(20.dp)
    )
  }
}
