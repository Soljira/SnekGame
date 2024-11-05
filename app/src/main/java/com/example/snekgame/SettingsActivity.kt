package com.example.snekgame

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

// Just for disabling bg music and sounds
class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Button initializations
        var checkMusic: ImageButton = findViewById(R.id.checkYesMusic)
        var checkSound: ImageButton = findViewById(R.id.checkYesSound)

        // Default state of the buttons
        var isCheckedMusic: Boolean = true
        var isCheckedSound: Boolean = true

        // Music button click listener
        checkMusic.setOnClickListener {
            isCheckedMusic = !isCheckedMusic // Toggle the state
            if (isCheckedMusic) {
                checkMusic.setImageResource(R.drawable.check_yes) // Set checked image
                // TODO: Enable background music
                // TODO: lEARN how to put music first lol
            } else {
                checkMusic.setImageResource(R.drawable.check_no) // Set unchecked image
                // TODO: Disable background music
            }
        }

        // Sound button click listener
        checkSound.setOnClickListener {
            isCheckedSound = !isCheckedSound // Toggle the state
            if (isCheckedSound) {
                checkSound.setImageResource(R.drawable.check_yes) // Set checked image
                // TODO: Enable sound effects
            } else {
                checkSound.setImageResource(R.drawable.check_no) // Set unchecked image
                // TODO: Disable sound effects
            }
        }
    }
}
