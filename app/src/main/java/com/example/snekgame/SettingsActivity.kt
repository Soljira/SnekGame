package com.example.snekgame

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.snekgame.helpers.BackgroundSoundService
import com.example.snekgame.helpers.SoundEffectsConstants

// Just for disabling bg music and sounds
class SettingsActivity : AppCompatActivity() {
    // Default state of the buttons
    var isMusicEnabled: Boolean = true
    var isSoundEnabled: Boolean = true

    private lateinit var checkSoundEffect: MediaPlayer
    private lateinit var uncheckSoundEffect: MediaPlayer


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Button initializations
        var checkMusic: ImageButton = findViewById(R.id.checkYesMusic)
        var checkSound: ImageButton = findViewById(R.id.checkYesSound)


        checkSoundEffect = MediaPlayer.create(this, SoundEffectsConstants.CHECK)
        uncheckSoundEffect = MediaPlayer.create(this, SoundEffectsConstants.UNCHECK)


        // Music button click listener
        checkMusic.setOnClickListener {
            isMusicEnabled = !isMusicEnabled // Toggle the state
            if (isMusicEnabled) {
                if (isSoundEnabled) {
                    checkSoundEffect.start()
                }
                checkMusic.setImageResource(R.drawable.check_yes) // Set checked image
                startMusicService()
            } else {
                if (isSoundEnabled) {
                    uncheckSoundEffect.start()
                }
                checkMusic.setImageResource(R.drawable.check_no) // Set unchecked image
                stopMusicService()
            }
        }

        // Sound button click listener (for enabling/disabling sound effects)
        checkSound.setOnClickListener {
            isSoundEnabled = !isSoundEnabled // Toggle the sound state
            checkSound.setImageResource(
                if (isSoundEnabled) {
                    checkSoundEffect.start()
                    R.drawable.check_yes
                } else {
                    R.drawable.check_no
                }
            )
            // TODO: Add logic to enable/disable sound effects
        }
    }

    fun startMusicService() {
        val intent = Intent(this, BackgroundSoundService::class.java)
        startService(intent)
    }

    fun stopMusicService() {
        val intent = Intent(this, BackgroundSoundService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resources
        checkSoundEffect.release()
        uncheckSoundEffect.release()
    }
}
