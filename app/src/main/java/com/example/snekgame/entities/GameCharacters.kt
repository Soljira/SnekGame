package com.example.snekgame.entities

import com.example.snekgame.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.snekgame.MainActivity
import com.example.snekgame.helpers.GameConstants
import com.example.snekgame.helpers.interfaces.BitmapMethods
import com.example.snekgame.helpers.interfaces.BitmapMethods.Companion.options

enum class GameCharacters(resID : Int) : BitmapMethods {

    PLAYER(R.drawable.player_spritesheet),
    SOUL(R.drawable.soul_spritesheet);

    val spriteSheet: Bitmap
    val sprites: Array<Array<Bitmap?>> = Array(4) { arrayOfNulls<Bitmap>(4) }

    init {
        options.inScaled = false  // Prevents the image from being scaled to fit the screen
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().resources, resID, options)

        // Extracts the selected sprites from the spritesheet
        for (j in sprites.indices) {
            for (i in sprites[j].indices) {
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(
                    spriteSheet,
                    GameConstants.Sprite.DEFAULT_SIZE * i,
                    GameConstants.Sprite.DEFAULT_SIZE * j,
                    GameConstants.Sprite.DEFAULT_SIZE,
                    GameConstants.Sprite.DEFAULT_SIZE
                ))
            }
        }
    }

    fun getSprite(yPos: Int, xPos: Int): Bitmap? {
        return sprites[yPos][xPos]
    }
}