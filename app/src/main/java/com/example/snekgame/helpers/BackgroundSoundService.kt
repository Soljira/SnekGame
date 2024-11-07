package com.example.snekgame.helpers

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder


class BackgroundSoundService : Service() {
    private lateinit var menuBg: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        menuBg = MediaPlayer.create(this, SoundEffectsConstants.MENU_BG)
        menuBg.isLooping = true  // Set looping
        menuBg.setVolume(1.0f, 1.0f)  // Set volume (1.0 is max volume in float format)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        menuBg.start()
        return START_STICKY  // Restart service if killed by the system
    }

    override fun onDestroy() {
        menuBg.stop()
        menuBg.release()
        super.onDestroy()
    }
}