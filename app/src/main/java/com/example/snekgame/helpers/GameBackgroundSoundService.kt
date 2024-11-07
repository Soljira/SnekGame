package com.example.snekgame.helpers

import com.example.snekgame.R
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder


class GameBackgroundSoundService : Service() {
    private lateinit var gameBg: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        gameBg = MediaPlayer.create(this, SoundEffectsConstants.GAME_BG)
        gameBg.isLooping = true  // Set looping
        gameBg.setVolume(1.0f, 1.0f)  // Set volume (1.0 is max volume in float format)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        gameBg.start()
        return START_STICKY  // Restart service if killed by the system
    }

    override fun onDestroy() {
        gameBg.stop()
        gameBg.release()
        super.onDestroy()
    }
}