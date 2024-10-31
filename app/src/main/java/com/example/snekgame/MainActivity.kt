package com.example.snekgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var isPressed:Boolean = false

        val intentPlayActivity = Intent(this, PlayActivity::class.java)
        // TODO: Make a new intent for each button
        val intentInstructionsActivity = Intent(this, InstructionsActivity::class.java)
        val intentAboutActivity = Intent(this, AboutActivity::class.java)

        // TODO: About button

        // TODO: Settings button



        // Changes the button image to a pressed version of it
        var buttonPlay:ImageButton = findViewById(R.id.btnPlay)
        var buttonInstructions:ImageButton = findViewById(R.id.btnInstructions)
        var buttonSettings:ImageButton = findViewById(R.id.btnSettings)
        var buttonAbout:ImageButton = findViewById(R.id.btnAbout)
        var buttonQuit:ImageButton = findViewById(R.id.btnQuit)

        // TODO: Make it so that button_play_pressed changes to button_play ONLY IF the user
        //       lets go of the button

        // TODO: Add a fire animation and music in the background
        buttonPlay.setOnClickListener {
            // If it's ugly, switch back to simple if-else statement and drop the delay
            if (!isPressed)
                isPressed = true
                buttonPlay.setBackgroundResource(R.drawable.button_play_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                isPressed = false
            }

            startActivity(intentPlayActivity)
        }

        buttonInstructions.setOnClickListener {
            // If it's ugly, switch back to simple if-else statement and drop the delay
            if (!isPressed)
                isPressed = true
            buttonInstructions.setBackgroundResource(R.drawable.button_instructions_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonInstructions.setBackgroundResource(R.drawable.button_instructions)
                isPressed = false
            }
        }

        buttonSettings.setOnClickListener {
            // If it's ugly, switch back to simple if-else statement and drop the delay
            if (!isPressed)
                isPressed = true
            buttonSettings.setBackgroundResource(R.drawable.button_settings_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonSettings.setBackgroundResource(R.drawable.button_settings)
                isPressed = false
            }
        }

        buttonAbout.setOnClickListener {
            // If it's ugly, switch back to simple if-else statement and drop the delay
            if (!isPressed)
                isPressed = true
            buttonAbout.setBackgroundResource(R.drawable.button_about_pressed)

            lifecycleScope.launch {
                delay(200)
                buttonAbout.setBackgroundResource(R.drawable.button_about)
                isPressed = false
            }
        }

        buttonQuit.setOnClickListener {
            // If it's ugly, switch back to simple if-else statement and drop the delay
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

    }
}