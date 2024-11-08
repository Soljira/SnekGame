package com.example.snekgame.entities.souls

import android.graphics.PointF
import com.example.snekgame.entities.Character
import com.example.snekgame.entities.GameCharacters
import com.example.snekgame.helpers.GameConstants
import kotlin.random.Random

// Put all soul code here

class BasicSoul (position : PointF) : Character(position, GameCharacters.SOUL) {
    private val random = Random.Default


    fun update(delta : Double) {



//  Basic souls dont move, but higher tier souls may. Create a Soul superclass in the future
//        updateMove()
        updateAnimation()
    }

    private fun updateMove(delta : Double) {

    }

    fun reset() {
        position = PointF(
            (150 + random.nextInt(GameConstants.Boundary.RIGHT - 150 + 1)).toFloat(),  // no idea why this works
            (410 + random.nextInt(GameConstants.Boundary.BOTTOM - 410 + 1)).toFloat()
        )
        resetAnimation()
    }
}   // End of BasicSoul class