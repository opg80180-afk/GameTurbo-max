package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
  @Query("SELECT * FROM games ORDER BY isFavorite DESC, lastLaunchedTimestamp DESC")
  fun getAllGames(): Flow<List<GameItem>>

  @Query("SELECT * FROM games WHERE packageName = :packageName LIMIT 1")
  fun getGame(packageName: String): Flow<GameItem?>

  @Query("SELECT COUNT(*) FROM games")
  suspend fun getGameCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGame(game: GameItem)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(games: List<GameItem>)

  @Update
  suspend fun updateGame(game: GameItem)

  @Delete
  suspend fun deleteGame(game: GameItem)

  @Query("DELETE FROM games WHERE packageName = :packageName")
  suspend fun deleteByPackageName(packageName: String)
}
