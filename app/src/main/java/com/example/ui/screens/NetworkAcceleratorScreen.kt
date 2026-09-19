package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.turbo.DnsConfig
import com.example.turbo.ServerPingNode
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
fun NetworkAcceleratorScreen(
  telemetry: SystemTelemetry,
  regionalPings: List<ServerPingNode>,
  isTestingNetwork: Boolean,
  selectedDns: String,
  dnsPresets: List<DnsConfig>,
  onRetestClick: () -> Unit,
  onSelectDns: (String) -> Unit,
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
    // Left Pane: Current Connection & Regional Server Nodes
    LazyColumn(
      modifier = Modifier
        .weight(0.48f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "CONNECTION SPEED",
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = telemetry.networkType,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Jitter: ${telemetry.networkJitterMs} ms",
                color = TurboGreen,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(TurboGreen.copy(alpha = 0.15f))
                .border(1.dp, TurboGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "${telemetry.networkPingMs} ms",
                  color = TurboGreen,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace
                )
                Text(
                  text = "OPTIMIZED",
                  color = TurboGreen,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      item {
        Text(
          text = "GLOBAL ESPORTS NODES PING",
          color = TextSecondary,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
      }

      items(regionalPings, key = { it.regionName }) { node ->
        RegionalNodeRow(node = node)
      }
    }

    // Right Pane: DNS Optimization & Retest Action
    LazyColumn(
      modifier = Modifier
        .weight(0.52f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "LOW-LATENCY DNS",
              color = TurboCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp
            )
            Text(
              text = "Packet Routing Optimization",
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Button(
            onClick = onRetestClick,
            enabled = !isTestingNetwork,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier.height(36.dp)
          ) {
            if (isTestingNetwork) {
              CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = TurboCyan,
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = TurboCyan,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isTestingNetwork) "TESTING..." else "RETEST",
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      items(dnsPresets, key = { it.name }) { dns ->
        val isSelected = selectedDns == dns.name
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) TurboCyan.copy(alpha = 0.12f) else SurfaceDark)
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) TurboCyan else SurfaceBorder,
              shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelectDns(dns.name) }
            .padding(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) TurboCyan.copy(alpha = 0.2f) else SurfaceElevated),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Dns,
                  contentDescription = null,
                  tint = if (isSelected) TurboCyan else TextSecondary,
                  modifier = Modifier.size(16.dp)
                )
              }

              Spacer(modifier = Modifier.width(8.dp))

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = dns.name,
                    color = if (isSelected) TurboCyan else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                  )
                  if (dns.isRecommended) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TurboAmber.copy(alpha = 0.2f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "ESPORTS",
                        color = TurboAmber,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                      )
                    }
                  }
                }
                Text(
                  text = "${dns.primaryIp} • ${dns.description}",
                  color = TextMuted,
                  fontSize = 10.sp
                )
              }
            }

            if (isSelected) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected",
                tint = TurboCyan,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RegionalNodeRow(node: ServerPingNode) {
  val pingColor = when {
    node.latencyMs < 45 -> TurboGreen
    node.latencyMs < 85 -> TurboAmber
    else -> TurboCrimson
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(SurfaceDark)
      .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
      .padding(horizontal = 10.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(text = node.flagEmoji, fontSize = 16.sp)
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = node.regionName,
          color = TextPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = node.host,
          color = TextMuted,
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .width(48.dp)
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(SurfaceElevated)
      ) {
        val barWidthRatio = (node.latencyMs / 120f).coerceIn(0.1f, 1f)
        Box(
          modifier = Modifier
            .fillMaxWidth(barWidthRatio)
            .height(5.dp)
            .background(pingColor)
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = "${node.latencyMs} ms",
        color = pingColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
      )
    }
  }
}
