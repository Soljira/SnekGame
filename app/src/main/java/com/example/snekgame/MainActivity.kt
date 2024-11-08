package com.example.snekgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess
import com.example.snekgame.helpers.BackgroundSoundService
import com.example.snekgame.helpers.SoundEffectsConstants

class MainActivity : AppCompatActivity() {
    // static class equivalent
    companion object {
        private lateinit var gameContext: Context  // no idea how to fix this lol

        fun getGameContext(): Context {
            return gameContext
        }
    }

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SuspiciousIndentation")

    // Intents for music and sounds
    lateinit var clickSound: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameContext = this
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var isPressed : Boolean = false

        val intentPlayActivity = Intent(this, GamePanelActivity::class.java)
        val intentAboutActivity = Intent(this, AboutActivity::class.java)
        val intentSettingsActivity = Intent(this, SettingsActivity::class.java)


        // Start background music service
        val musicServiceIntent = Intent(this, BackgroundSoundService::class.java)
        startService(musicServiceIntent) // Start playing background music

        clickSound = MediaPlayer.create(this, SoundEffectsConstants.CLICK)

        // Button initializations
        var buttonPlay : ImageButton = findViewById(R.id.btnPlay)
        var buttonSettings : ImageButton = findViewById(R.id.btnSettings)
        var buttonAbout : ImageButton = findViewById(R.id.btnAbout)
        var buttonQuit : ImageButton = findViewById(R.id.btnQuit)

        // TODO: Make it so that button_play_pressed changes to button_play ONLY IF the user lets go of the button
        // TODO: Add a fire animation and music in the background

        // Note to self: find a way to make these less repetitive. Maybe switch cases
        buttonPlay.setOnClickListener {
            clickSound.start()
            // If it's ugly, switch back to simple if-else statement and drop the delay
            if (!isPressed)
                isPressed = true
            buttonPlay.setBackgroundResource(R.drawable.button_play_pressed)    // Changes the button image to a pressed version of it

            lifecycleScope.launch {
                delay(200)
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                isPressed = false
            }

            startActivity(intentPlayActivity)
//            setContentView(GamePanel(this))
        }

        buttonSettings.setOnClickListener {
            clickSound.start()

            if (!isPressed)
                isPressed = true
            buttonSettings.setBackgroundResource(R.drawable.button_settings_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonSettings.setBackgroundResource(R.drawable.button_settings)
                isPressed = false
            }
            startActivity(intentSettingsActivity)
        }

        buttonAbout.setOnClickListener {
            clickSound.start()

            if (!isPressed)
                isPressed = true
            buttonAbout.setBackgroundResource(R.drawable.button_about_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonAbout.setBackgroundResource(R.drawable.button_about)
                isPressed = false
            }
            startActivity(intentAboutActivity)
        }

        buttonQuit.setOnClickListener {
            clickSound.start()

            if (!isPressed)
                isPressed = true
            buttonQuit.setBackgroundResource(R.drawable.button_quit_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonQuit.setBackgroundResource(R.drawable.button_quit)
                isPressed = false
                exitProcess(0)
            }

        }

    }  // End of onCreate class


    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resources
        clickSound.release()
    }
}