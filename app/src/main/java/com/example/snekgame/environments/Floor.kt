package com.example.snekgame.environments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.snekgame.MainActivity
import com.example.snekgame.R
import com.example.snekgame.helpers.GameConstants
import com.example.snekgame.helpers.interfaces.BitmapMethods
import com.example.snekgame.helpers.interfaces.BitmapMethods.Companion.options

enum class Floor : BitmapMethods {

    OUTSIDE(R.drawable.tileset_floor, 22, 26);

    private val sprites : Array<Bitmap>

    constructor(resID: Int, tilesInWidth: Int, tilesInHeight: Int) {
        options.inScaled = false
        sprites = Array(tilesInHeight * tilesInWidth) { Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) } // Initialize with dummy bitmaps

        val spriteSheet = BitmapFactory.decodeResource(
            MainActivity.getGameContext().resources,
            resID,
            options
        )

        // Extracts the selected sprites from the spritesheet
        for (j in 0 until tilesInHeight) {
            for (i in 0 until tilesInWidth) {
                val index = j * tilesInWidth + i
                sprites[index] = getScaledBitmap(
                    Bitmap.createBitmap(
                        spriteSheet,
                        GameConstants.Sprite.DEFAULT_SIZE * i,
                        GameConstants.Sprite.DEFAULT_SIZE * j,
                        GameConstants.Sprite.DEFAULT_SIZE,
                        GameConstants.Sprite.DEFAULT_SIZE
                    )
                )
            }
        }
    }

    fun getSprite(id: Int): Bitmap {
        return sprites[id]
    }
}
