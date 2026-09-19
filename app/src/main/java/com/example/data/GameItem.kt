package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameItem(
  @PrimaryKey
  val packageName: String,
  val title: String,
  val category: String = "Action RPG",
  val bannerDrawableName: String? = null,
  val performanceMode: String = "TURBO_EXTREME", // TURBO_EXTREME, PERFORMANCE, BALANCED, BATTERY_SAVER
  val targetFps: Int = 120,
  val touchHz: Int = 240,
  val blockNotifications: Boolean = true,
  val rejectCalls: Boolean = true,
  val lockBrightness: Boolean = true,
  val hapticFeedback: Boolean = true,
  val networkPriority: Boolean = true,
  val visualEnhancement: String = "HDR_VIVID", // HDR_VIVID, SHADOW_BOOST, EXTREME_CONTRAST, STANDARD
  val playTimeMinutes: Int = 45,
  val lastLaunchedTimestamp: Long = System.currentTimeMillis(),
  val isFavorite: Boolean = false
)
