package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [GameItem::class], version = 1, exportSchema = false)
abstract class GameTurboDatabase : RoomDatabase() {
  abstract fun gameDao(): GameDao

  companion object {
    @Volatile
    private var INSTANCE: GameTurboDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): GameTurboDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GameTurboDatabase::class.java,
          "game_turbo_database"
        )
        .addCallback(object : Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
              scope.launch(Dispatchers.IO) {
                populateInitialGames(database.gameDao())
              }
            }
          }
        })
        .fallbackToDestructiveMigration()
        .build()
        INSTANCE = instance
        instance
      }
    }

    private suspend fun populateInitialGames(dao: GameDao) {
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
      dao.insertAll(initialGames)
    }
  }
}
