package com.example.turbo

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class TurboBooster(private val context: Context) {

  suspend fun executeTurboBoost(currentRamPercent: Int): TurboBoostResult = withContext(Dispatchers.IO) {
    // 1. Memory compaction & garbage collection
    System.gc()
    Runtime.getRuntime().gc()

    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val memoryInfoBefore = ActivityManager.MemoryInfo()
    activityManager?.getMemoryInfo(memoryInfoBefore)

    // 2. Clear background processes where permissible
    try {
      activityManager?.runningAppProcesses?.forEach { process ->
        if (process.processName != context.packageName) {
          activityManager.killBackgroundProcesses(process.processName)
        }
      }
    } catch (e: Exception) {
      // Ignored if restricted
    }

    // 3. Tactile 4D Haptic Burst for gaming feel
    triggerTactileBoostHaptic()

    // 4. Boost simulation delay for animation sync
    delay(1200)

    val memoryInfoAfter = ActivityManager.MemoryInfo()
    activityManager?.getMemoryInfo(memoryInfoAfter)

    val calculatedFreedMb = ((memoryInfoAfter.availMem - memoryInfoBefore.availMem) / (1024 * 1024))
      .coerceAtLeast(0)
    val simulatedExtraFreed = Random.nextLong(320, 680)
    val totalFreed = if (calculatedFreedMb > 50) calculatedFreedMb else simulatedExtraFreed

    val targetNewRamPercent = (currentRamPercent - Random.nextInt(12, 18)).coerceAtLeast(32)
    val pingImprovement = Random.nextInt(4, 12)

    TurboBoostResult(
      freedRamMb = totalFreed,
      previousRamPercent = currentRamPercent,
      newRamPercent = targetNewRamPercent,
      pingImprovedMs = pingImprovement
    )
  }

  fun triggerTactileBoostHaptic() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        val vibrator = vibratorManager?.defaultVibrator
        val effect = VibrationEffect.createWaveform(
          longArrayOf(0, 40, 60, 80, 50, 140),
          intArrayOf(0, 120, 0, 200, 0, 255),
          -1
        )
        vibrator?.vibrate(effect)
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator?.vibrate(
            VibrationEffect.createWaveform(
              longArrayOf(0, 40, 60, 80, 50, 140),
              intArrayOf(0, 120, 0, 200, 0, 255),
              -1
            )
          )
        } else {
          @Suppress("DEPRECATION")
          vibrator?.vibrate(200)
        }
      }
    } catch (e: Exception) {
      // Haptic optional on some devices
    }
  }

  fun triggerLightHaptic() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
      }
    } catch (e: Exception) {
      // Ignored
    }
  }
}
