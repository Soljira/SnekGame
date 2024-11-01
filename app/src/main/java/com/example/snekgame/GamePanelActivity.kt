package com.example.snekgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GamePanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the GamePanel and set it as the content view
        val gamePanel = GamePanel(this)
        setContentView(gamePanel)
    }
}