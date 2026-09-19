package com.example.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

data class InstalledApp(
  val packageName: String,
  val label: String,
  val icon: Drawable? = null,
  val isGame: Boolean = false
)

class GameTurboRepository(
  private val context: Context,
  private val gameDao: GameDao
) {
  val allGames: Flow<List<GameItem>> = gameDao.getAllGames()

  suspend fun ensureDefaultGames() {
    withContext(Dispatchers.IO) {
      if (gameDao.getGameCount() == 0) {
        val initialGames = listOf(
          GameItem(
            packageName = "com.gameturbo.cyberstrike",
            title = "CyberStrike 2088",
            category = "Action RPG",
            bannerDrawableName = "img_game_cyberstrike",
            performanceMode = "TURBO_EXTREME",
            targetFps = 120,
            touchHz = 480,
            blockNotifications = true,
            rejectCalls = true,
            lockBrightness = true,
            hapticFeedback = true,
            networkPriority = true,
            visualEnhancement = "HDR_VIVID",
            playTimeMinutes = 184,
            isFavorite = true
          ),
          GameItem(
            packageName = "com.gameturbo.speedstorm",
            title = "SpeedStorm: Neon Overdrive",
            category = "Supersonic Racing",
            bannerDrawableName = "img_game_speedstorm",
            performanceMode = "TURBO_EXTREME",
            targetFps = 144,
            touchHz = 480,
            blockNotifications = true,
            rejectCalls = true,
            lockBrightness = true,
            hapticFeedback = true,
            networkPriority = true,
            visualEnhancement = "SHADOW_BOOST",
            playTimeMinutes = 112,
            isFavorite = true
          ),
          GameItem(
            packageName = "com.mihoyo.genshinimpact",
            title = "Genshin Impact",
            category = "Open World RPG",
            bannerDrawableName = "img_turbo_hero",
            performanceMode = "PERFORMANCE",
            targetFps = 60,
            touchHz = 240,
            blockNotifications = true,
            rejectCalls = true,
            lockBrightness = false,
            hapticFeedback = false,
            networkPriority = true,
            visualEnhancement = "EXTREME_CONTRAST",
            playTimeMinutes = 320,
            isFavorite = false
          ),
          GameItem(
            packageName = "com.tencent.ig",
            title = "PUBG Mobile Ultra",
            category = "Battle Royale",
            bannerDrawableName = "img_turbo_icon",
            performanceMode = "TURBO_EXTREME",
            targetFps = 90,
            touchHz = 480,
            blockNotifications = true,
            rejectCalls = true,
            lockBrightness = true,
            hapticFeedback = true,
            networkPriority = true,
            visualEnhancement = "SHADOW_BOOST",
            playTimeMinutes = 245,
            isFavorite = false
          )
        )
        gameDao.insertAll(initialGames)
      }
    }
  }

  suspend fun insertGame(game: GameItem) = withContext(Dispatchers.IO) {
    gameDao.insertGame(game)
  }

  suspend fun updateGame(game: GameItem) = withContext(Dispatchers.IO) {
    gameDao.updateGame(game)
  }

  suspend fun deleteGame(game: GameItem) = withContext(Dispatchers.IO) {
    gameDao.deleteGame(game)
  }

  suspend fun getInstalledLaunchableApps(): List<InstalledApp> = withContext(Dispatchers.IO) {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
      addCategory(Intent.CATEGORY_LAUNCHER)
    }
    val resolveInfos = pm.queryIntentActivities(intent, 0)
    val result = mutableListOf<InstalledApp>()
    for (resolve in resolveInfos) {
      val pkg = resolve.activityInfo.packageName
      if (pkg == context.packageName) continue // skip self
      val label = resolve.loadLabel(pm).toString()
      val icon = resolve.loadIcon(pm)
      val appInfo = try {
        pm.getApplicationInfo(pkg, 0)
      } catch (e: Exception) {
        null
      }
      val isGame = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        appInfo?.category == ApplicationInfo.CATEGORY_GAME
      } else {
        false
      }
      result.add(InstalledApp(packageName = pkg, label = label, icon = icon, isGame = isGame))
    }
    result.sortedWith(compareByDescending<InstalledApp> { it.isGame }.thenBy { it.label })
  }

  fun launchGame(packageName: String): Boolean {
    val pm = context.packageManager
    val launchIntent = pm.getLaunchIntentForPackage(packageName)
    return if (launchIntent != null) {
      launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(launchIntent)
      true
    } else {
      false
    }
  }
}
