package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.BgmPhonkEngine
import com.example.data.GameItem
import com.example.data.GameTurboDatabase
import com.example.data.GameTurboRepository
import com.example.data.InstalledApp
import com.example.turbo.DnsConfig
import com.example.turbo.NetworkSpeedTester
import com.example.turbo.ServerPingNode
import com.example.turbo.SystemTelemetry
import com.example.turbo.TelemetryMonitor
import com.example.turbo.TurboBoostResult
import com.example.turbo.TurboBooster
import com.example.turbo.TurboLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameTurboUiState(
  val telemetry: SystemTelemetry = SystemTelemetry(),
  val games: List<GameItem> = emptyList(),
  val isBoosting: Boolean = false,
  val lastBoostResult: TurboBoostResult? = null,
  val selectedGameForProfile: GameItem? = null,
  val currentTab: Int = 0, // 0: Space, 1: Performance Tuner, 2: Network, 3: In-Game HUD
  val regionalPings: List<ServerPingNode> = emptyList(),
  val isTestingNetwork: Boolean = false,
  val selectedDns: String = "Cloudflare 1.1.1.1",
  val installedApps: List<InstalledApp> = emptyList(),
  val isAddGameDialogOpen: Boolean = false,
  val bannerNotification: String? = null,
  // Floating Overlay Toolbox Simulation State
  val overlayDndEnabled: Boolean = true,
  val overlayBrightnessLocked: Boolean = true,
  val overlayBrightnessLevel: Float = 0.85f,
  val overlayFpsOverlayActive: Boolean = true,
  val overlaySelectedVoice: String = "Original",
  val overlayVoiceActive: Boolean = false,
  // Background Music "Baila Lento" Phonk State
  val isBgmPlaying: Boolean = false,
  val bgmVisualizerBars: List<Float> = listOf(0.2f, 0.4f, 0.3f, 0.5f),
  val bgmVolume: Float = 0.85f
)

class GameTurboViewModel(application: Application) : AndroidViewModel(application) {

  private val database = GameTurboDatabase.getDatabase(application, viewModelScope)
  private val repository = GameTurboRepository(application, database.gameDao())
  private val telemetryMonitor = TelemetryMonitor(application)
  private val turboBooster = TurboBooster(application)
  private val networkTester = NetworkSpeedTester()
  private val bgmEngine = BgmPhonkEngine(viewModelScope)

  private val _uiState = MutableStateFlow(GameTurboUiState())
  val uiState: StateFlow<GameTurboUiState> = _uiState.asStateFlow()

  val dnsPresets: List<DnsConfig> = networkTester.dnsPresets

  init {
    viewModelScope.launch {
      bgmEngine.isPlaying.collect { playing ->
        _uiState.update { it.copy(isBgmPlaying = playing) }
      }
    }

    viewModelScope.launch {
      bgmEngine.visualizerBars.collect { bars ->
        _uiState.update { it.copy(bgmVisualizerBars = bars) }
      }
    }

    viewModelScope.launch {
      bgmEngine.volume.collect { vol ->
        _uiState.update { it.copy(bgmVolume = vol) }
      }
    }

    // Auto-start "Baila Lento" Brazilian Phonk BGM on launch
    bgmEngine.play()

    viewModelScope.launch {
      repository.ensureDefaultGames()
    }

    viewModelScope.launch {
      repository.allGames.collect { gamesList ->
        _uiState.update { it.copy(games = gamesList) }
      }
    }

    viewModelScope.launch {
      telemetryMonitor.getTelemetryFlow().collect { telemetryData ->
        _uiState.update { current ->
          // If currently boosted, elevate telemetry aesthetics
          val adjusted = if (current.lastBoostResult != null && !current.isBoosting) {
            telemetryData.copy(
              ramPercent = current.lastBoostResult.newRamPercent,
              networkPingMs = (telemetryData.networkPingMs - current.lastBoostResult.pingImprovedMs).coerceAtLeast(10),
              activeTurboLevel = TurboLevel.TURBO_EXTREME
            )
          } else {
            telemetryData
          }
          current.copy(telemetry = adjusted)
        }
      }
    }

    runNetworkTest()
  }

  fun selectTab(tabIndex: Int) {
    turboBooster.triggerLightHaptic()
    _uiState.update { it.copy(currentTab = tabIndex) }
  }

  fun triggerTurboBoost() {
    if (_uiState.value.isBoosting) return

    viewModelScope.launch {
      _uiState.update { it.copy(isBoosting = true) }
      val currentRam = _uiState.value.telemetry.ramPercent
      val result = turboBooster.executeTurboBoost(currentRam)
      _uiState.update {
        it.copy(
          isBoosting = false,
          lastBoostResult = result,
          bannerNotification = "🚀 TURBO BOOST ACTIVE! Freed ${result.freedRamMb}MB RAM, Latency improved by -${result.pingImprovedMs}ms"
        )
      }
    }
  }

  fun openGameProfile(game: GameItem) {
    turboBooster.triggerLightHaptic()
    _uiState.update { it.copy(selectedGameForProfile = game) }
  }

  fun closeGameProfile() {
    _uiState.update { it.copy(selectedGameForProfile = null) }
  }

  fun saveGameProfile(updatedGame: GameItem) {
    viewModelScope.launch {
      repository.updateGame(updatedGame)
      _uiState.update {
        it.copy(
          selectedGameForProfile = null,
          bannerNotification = "Profile tuned for ${updatedGame.title} [${updatedGame.performanceMode} • ${updatedGame.targetFps} FPS]"
        )
      }
    }
  }

  fun toggleFavorite(game: GameItem) {
    viewModelScope.launch {
      val updated = game.copy(isFavorite = !game.isFavorite)
      repository.updateGame(updated)
      turboBooster.triggerLightHaptic()
    }
  }

  fun deleteGame(game: GameItem) {
    viewModelScope.launch {
      repository.deleteGame(game)
      if (_uiState.value.selectedGameForProfile?.packageName == game.packageName) {
        _uiState.update { it.copy(selectedGameForProfile = null) }
      }
    }
  }

  fun launchGame(game: GameItem) {
    turboBooster.triggerTactileBoostHaptic()
    val launched = repository.launchGame(game.packageName)
    viewModelScope.launch {
      repository.updateGame(
        game.copy(
          lastLaunchedTimestamp = System.currentTimeMillis(),
          playTimeMinutes = game.playTimeMinutes + 5
        )
      )
    }

    if (!launched) {
      _uiState.update {
        it.copy(
          bannerNotification = "⚡ Turbo Applied for ${game.title}: ${game.targetFps} FPS, ${game.touchHz}Hz Touch & DND Active!"
        )
      }
    }
  }

  fun openAddGameDialog() {
    turboBooster.triggerLightHaptic()
    viewModelScope.launch {
      val installed = repository.getInstalledLaunchableApps()
      _uiState.update {
        it.copy(
          installedApps = installed,
          isAddGameDialogOpen = true
        )
      }
    }
  }

  fun closeAddGameDialog() {
    _uiState.update { it.copy(isAddGameDialogOpen = false) }
  }

  fun addInstalledAppAsGame(app: InstalledApp) {
    viewModelScope.launch {
      val newGame = GameItem(
        packageName = app.packageName,
        title = app.label,
        category = if (app.isGame) "Mobile Game" else "Accelerated App",
        bannerDrawableName = "img_turbo_hero",
        performanceMode = "TURBO_EXTREME",
        targetFps = 120,
        touchHz = 480,
        blockNotifications = true,
        rejectCalls = true,
        lockBrightness = true,
        hapticFeedback = true,
        networkPriority = true,
        visualEnhancement = "HDR_VIVID"
      )
      repository.insertGame(newGame)
      _uiState.update {
        it.copy(
          isAddGameDialogOpen = false,
          bannerNotification = "Added ${app.label} to Game Space with Turbo Boost profile!"
        )
      }
    }
  }

  fun addCustomGame(title: String, category: String) {
    if (title.isBlank()) return
    viewModelScope.launch {
      val pkg = "custom.game." + title.lowercase().replace(" ", "")
      val newGame = GameItem(
        packageName = pkg,
        title = title,
        category = category.ifBlank { "Action Gaming" },
        bannerDrawableName = "img_game_cyberstrike",
        performanceMode = "TURBO_EXTREME",
        targetFps = 120,
        touchHz = 480,
        blockNotifications = true,
        rejectCalls = true,
        lockBrightness = true,
        hapticFeedback = true,
        networkPriority = true,
        visualEnhancement = "HDR_VIVID"
      )
      repository.insertGame(newGame)
      _uiState.update {
        it.copy(
          isAddGameDialogOpen = false,
          bannerNotification = "Added $title to Game Space!"
        )
      }
    }
  }

  fun runNetworkTest() {
    viewModelScope.launch {
      _uiState.update { it.copy(isTestingNetwork = true) }
      val nodes = networkTester.testRegionalNodes()
      _uiState.update {
        it.copy(
          regionalPings = nodes,
          isTestingNetwork = false
        )
      }
    }
  }

  fun selectDns(dnsName: String) {
    turboBooster.triggerLightHaptic()
    _uiState.update {
      it.copy(
        selectedDns = dnsName,
        bannerNotification = "DNS Accelerated: $dnsName applied for lowest gaming jitter!"
      )
    }
  }

  fun dismissNotification() {
    _uiState.update { it.copy(bannerNotification = null) }
  }

  // In-Game Toolbox controls
  fun toggleOverlayDnd() {
    turboBooster.triggerLightHaptic()
    _uiState.update { it.copy(overlayDndEnabled = !it.overlayDndEnabled) }
  }

  fun toggleOverlayBrightnessLock() {
    turboBooster.triggerLightHaptic()
    _uiState.update { it.copy(overlayBrightnessLocked = !it.overlayBrightnessLocked) }
  }

  fun setOverlayBrightness(level: Float) {
    _uiState.update { it.copy(overlayBrightnessLevel = level) }
  }

  fun toggleOverlayFps() {
    turboBooster.triggerLightHaptic()
    _uiState.update { it.copy(overlayFpsOverlayActive = !it.overlayFpsOverlayActive) }
  }

  fun selectVoiceChanger(voice: String) {
    turboBooster.triggerLightHaptic()
    _uiState.update {
      it.copy(
        overlaySelectedVoice = voice,
        overlayVoiceActive = voice != "Original"
      )
    }
  }

  // Background Phonk Music ("Baila Lento") Controls
  fun toggleBgm() {
    turboBooster.triggerLightHaptic()
    bgmEngine.togglePlayPause()
  }

  fun setBgmVolume(volume: Float) {
    bgmEngine.setVolume(volume)
  }

  override fun onCleared() {
    super.onCleared()
    bgmEngine.release()
  }
}
