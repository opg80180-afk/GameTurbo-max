package com.example.turbo

enum class TurboLevel(val title: String, val badge: String, val boostFactor: Float) {
  BALANCED("Balanced", "STABLE", 1.0f),
  PERFORMANCE("Performance", "PRO", 1.3f),
  TURBO_EXTREME("Turbo Extreme", "EXTREME", 1.75f)
}

data class SystemTelemetry(
  val cpuUsagePercent: Int = 34,
  val cpuTempCelsius: Float = 38.5f,
  val cpuFrequencyGhz: Float = 2.84f,
  val ramUsedMb: Long = 4210,
  val ramTotalMb: Long = 7800,
  val ramPercent: Int = 54,
  val batteryLevelPercent: Int = 82,
  val batteryTempCelsius: Float = 32.4f,
  val isCharging: Boolean = false,
  val networkPingMs: Int = 24,
  val networkJitterMs: Int = 3,
  val networkType: String = "Wi-Fi 6 (5GHz)",
  val currentFps: Int = 120,
  val targetFps: Int = 120,
  val activeTurboLevel: TurboLevel = TurboLevel.TURBO_EXTREME,
  val isBoostActive: Boolean = false
)

data class TurboBoostResult(
  val freedRamMb: Long,
  val previousRamPercent: Int,
  val newRamPercent: Int,
  val pingImprovedMs: Int,
  val boostTimestamp: Long = System.currentTimeMillis()
)

data class ServerPingNode(
  val regionName: String,
  val flagEmoji: String,
  val host: String,
  val port: Int = 53,
  val latencyMs: Int = 0,
  val status: String = "IDLE" // IDLE, TESTING, OPTIMAL, SLOW
)
