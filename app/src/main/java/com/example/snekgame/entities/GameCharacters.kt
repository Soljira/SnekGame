package com.example.snekgame.entities

import com.example.snekgame.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.snekgame.MainActivity

enum class GameCharacters(resID : Int) {

    PLAYER(R.drawable.player_spritesheet),
    SOUL(R.drawable.soul_spritesheet);

    val spriteSheet: Bitmap
    val sprites: Array<Array<Bitmap?>> = Array(4) { arrayOfNulls<Bitmap>(4) }
    val options = BitmapFactory.Options()

    init {
        options.inScaled = false  // Prevents the image from being scaled to fit the screen
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().resources, resID, options)

        // Extracts the selected sprites from the spritesheet
        for (j in sprites.indices) {
            for (i in sprites[j].indices) {
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, 16 * i, 16 * j, 16, 16))
            }
        }
    }

    fun getSprite(yPos: Int, xPos: Int): Bitmap? {
        return sprites[yPos][xPos]
    }


    fun getScaledBitmap(bitmap : Bitmap) : Bitmap {
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 6, bitmap.getHeight() * 6, false)
    }
}