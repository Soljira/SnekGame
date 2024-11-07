package com.example.snekgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snekgame.helpers.BackgroundSoundService
import com.example.snekgame.helpers.GameBackgroundSoundService

class GamePanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the GamePanel and set it as the content view
        val gamePanel = GamePanel(this)
        setContentView(gamePanel)

        val musicServiceIntent = Intent(this, BackgroundSoundService::class.java)
        val gameMusicServiceIntent = Intent(this, GameBackgroundSoundService::class.java)
        stopService(musicServiceIntent) // Stops playing background music
        val settings = SettingsActivity()

        if (settings.isMusicEnabled)
            startService(gameMusicServiceIntent) // Starts playing background music

    }
}