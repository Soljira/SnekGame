package com.example.snekgame.environments

import android.graphics.Canvas
import com.example.snekgame.helpers.GameConstants


/**
 * Stores the world, dictates which tiles go where, and draw them onto the canvas
 */

class GameMap(private val spriteIds: Array<IntArray>) {
    // Stores the world in IDs
//    private val spriteIds: Array<Array<Bitmap?>> = Array(4) { arrayOfNulls<Bitmap>(4) }

    fun draw(canvas : Canvas) {
        for (j in spriteIds.indices) {
            for (i in spriteIds[j].indices) {
                canvas.drawBitmap(Floor.OUTSIDE.getSprite(
                    spriteIds[j][i]),
                    i * GameConstants.Sprite.SIZE.toFloat(),
                    j * GameConstants.Sprite.SIZE.toFloat(),
                    null)
            }
        }
    }  // End of draw function
}  // End of GameMap class