package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Procedural Real-time Audio Engine for "Baila Lento" Brazilian Phonk.
 * Synthesizes heavy 808 sub-bass, syncopated baile-funk kicks, distorted phonk cowbells,
 * and trap hi-hats in real-time using Android AudioTrack PCM.
 */
class BgmPhonkEngine(private val scope: CoroutineScope) {

  private var audioTrack: AudioTrack? = null
  private var playbackJob: Job? = null

  private val sampleRate = 44100
  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _volume = MutableStateFlow(0.85f)
  val volume: StateFlow<Float> = _volume.asStateFlow()

  // Real-time amplitude values (4 bars) for the visualizer
  private val _visualizerBars = MutableStateFlow(listOf(0.2f, 0.4f, 0.3f, 0.5f))
  val visualizerBars: StateFlow<List<Float>> = _visualizerBars.asStateFlow()

  init {
    initAudioTrack()
  }

  private fun initAudioTrack() {
    try {
      val minBufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
      )

      val bufferSize = (minBufferSize * 2).coerceAtLeast(8192)

      audioTrack = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(bufferSize)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()

      audioTrack?.setVolume(_volume.value)
    } catch (e: Exception) {
      Log.e("BgmPhonkEngine", "Failed to init AudioTrack", e)
    }
  }

  fun togglePlayPause() {
    if (_isPlaying.value) {
      pause()
    } else {
      play()
    }
  }

  fun play() {
    if (_isPlaying.value) return
    if (audioTrack == null || audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
      initAudioTrack()
    }

    try {
      audioTrack?.play()
      _isPlaying.value = true

      playbackJob = scope.launch(Dispatchers.Default) {
        generatePhonkLoop()
      }
    } catch (e: Exception) {
      Log.e("BgmPhonkEngine", "Error starting playback", e)
    }
  }

  fun pause() {
    _isPlaying.value = false
    playbackJob?.cancel()
    playbackJob = null
    try {
      audioTrack?.pause()
      audioTrack?.flush()
      _visualizerBars.value = listOf(0.1f, 0.1f, 0.1f, 0.1f)
    } catch (e: Exception) {
      Log.e("BgmPhonkEngine", "Error pausing playback", e)
    }
  }

  fun setVolume(vol: Float) {
    val clamped = vol.coerceIn(0f, 1f)
    _volume.value = clamped
    audioTrack?.setVolume(clamped)
  }

  fun release() {
    pause()
    try {
      audioTrack?.release()
      audioTrack = null
    } catch (e: Exception) {
      Log.e("BgmPhonkEngine", "Error releasing AudioTrack", e)
    }
  }

  /**
   * Procedural Audio Loop for Brazilian Phonk "Baila Lento" style.
   * 130 BPM tempo. 4 beats per bar, 2-bar repeating riff (32 sixteenth-notes).
   */
  private suspend fun generatePhonkLoop() {
    val bpm = 130
    val sixteenthNoteSamples = (sampleRate * 60f / bpm / 4f).toInt()
    val totalSteps = 32 // 2 bars
    val totalSamples = sixteenthNoteSamples * totalSteps

    // Pre-calculate the 2-bar PCM audio buffer
    val pcmBuffer = ShortArray(totalSamples)

    // Cowbell Melody (frequencies in Hz for Phonk scale in D minor / F major)
    // Classic 808 cowbell uses twin square-wave oscillators around 540Hz and 800Hz
    val cowbellPitches = floatArrayOf(
      587.33f, // D5
      587.33f, // D5
      698.46f, // F5
      783.99f, // G5
      880.00f, // A5
      783.99f, // G5
      698.46f, // F5
      659.25f, // E5
      587.33f, // D5
      587.33f, // D5
      698.46f, // F5
      880.00f, // A5
      1046.50f,// C6
      880.00f, // A5
      783.99f, // G5
      698.46f  // F5
    )

    // Baile Funk / Brazilian Phonk Kick rhythm steps (16th notes):
    // Standard Baile funk rhythm: 0, 3, 4, 6, 8, 11, 12, 14, 16, 19, 20, 22, 24, 27, 28, 30
    val kickSteps = setOf(0, 3, 4, 6, 8, 11, 12, 14, 16, 19, 20, 22, 24, 27, 28, 30)

    // Snare / Clap on beat 2 and 4 of each bar: steps 4, 12, 20, 28
    val snareSteps = setOf(4, 12, 20, 28)

    // Cowbell triggers (every 2 steps with syncopated accents)
    val cowbellSteps = intArrayOf(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30)

    for (step in 0 until totalSteps) {
      val startSample = step * sixteenthNoteSamples
      val isKick = kickSteps.contains(step)
      val isSnare = snareSteps.contains(step)
      val cowbellIndex = cowbellSteps.indexOf(step)
      val cowbellPitch = if (cowbellIndex >= 0) cowbellPitches[cowbellIndex % cowbellPitches.size] else 0f

      for (i in 0 until sixteenthNoteSamples) {
        val sampleIndex = startSample + i
        val t = i.toFloat() / sampleRate
        var mixedSample = 0f

        // 1. Kick & 808 Sub-Bass (Heavy saturated sine wave with pitch drop)
        if (isKick) {
          val kickEnvelope = exp(-t * 14f)
          val kickPitch = 55f + 120f * exp(-t * 28f) // Pitch drop from 175Hz to 55Hz (Sub-bass)
          val rawKick = sin(2f * PI.toFloat() * kickPitch * t)
          // Hard clipping distortion characteristic of Phonk
          val distortedKick = (rawKick * 1.6f).coerceIn(-0.95f, 0.95f)
          mixedSample += distortedKick * kickEnvelope * 0.75f
        }

        // 2. Snare / Clap (White noise + tone burst with exponential decay)
        if (isSnare) {
          val snareEnvelope = exp(-t * 26f)
          val noise = (Random.nextFloat() * 2f - 1f) * 0.7f
          val body = sin(2f * PI.toFloat() * 210f * t) * 0.3f
          mixedSample += (noise + body) * snareEnvelope * 0.65f
        }

        // 3. Iconic Phonk Cowbell (Metallic Dual-oscillator with crisp attack & decay)
        if (cowbellPitch > 0f) {
          val cowbellEnvelope = exp(-t * 9f)
          // Phonk cowbell harmonic frequencies
          val osc1 = sin(2f * PI.toFloat() * cowbellPitch * t)
          val osc2 = sin(2f * PI.toFloat() * (cowbellPitch * 1.48f) * t) * 0.6f
          val osc3 = sin(2f * PI.toFloat() * (cowbellPitch * 2.01f) * t) * 0.35f
          // Overdrive punch
          val cowbellSound = ((osc1 + osc2 + osc3) * 1.4f).coerceIn(-1f, 1f)
          mixedSample += cowbellSound * cowbellEnvelope * 0.55f
        }

        // 4. Trap / Phonk Hi-Hat (crisp high metallic tick on every 16th note)
        val hiHatEnv = exp(-t * 60f)
        val hiHatNoise = (Random.nextFloat() * 2f - 1f) * 0.25f
        mixedSample += hiHatNoise * hiHatEnv * 0.4f

        // Convert mixed float sample to 16-bit PCM short
        val finalSample = (mixedSample.coerceIn(-1.0f, 1.0f) * 32767f).toInt().toShort()
        pcmBuffer[sampleIndex] = finalSample
      }
    }

    // Streaming loop to AudioTrack with visualizer updates
    val chunkDurationMs = (sixteenthNoteSamples * 1000L / sampleRate)
    var stepCounter = 0

    while (scope.isActive && _isPlaying.value) {
      val start = (stepCounter % totalSteps) * sixteenthNoteSamples
      val written = audioTrack?.write(pcmBuffer, start, sixteenthNoteSamples) ?: 0
      if (written < 0) break

      // Update Visualizer bars to the rhythm
      val isHeavy = kickSteps.contains(stepCounter % totalSteps)
      val isSnareNow = snareSteps.contains(stepCounter % totalSteps)

      _visualizerBars.value = listOf(
        if (isHeavy) 0.95f else 0.35f,
        if (isSnareNow) 0.9f else 0.45f,
        if (isHeavy) 0.85f else 0.55f,
        if (stepCounter % 2 == 0) 0.75f else 0.25f
      )

      stepCounter++
      kotlinx.coroutines.delay((chunkDurationMs * 0.85f).toLong())
    }
  }
}
