package com.example.snekgame.entities.souls

import android.graphics.PointF
import com.example.snekgame.entities.Character
import com.example.snekgame.entities.GameCharacters

// Put all soul code here

class BasicSoul (position : PointF) : Character(position, GameCharacters.SOUL) {
    fun update(delta : Double) {



//  Basic souls dont move, but higher tier souls may. Create a Soul superclass in the future
//        updateMove()
        updateAnimation()
    }

    private fun updateMove(delta : Double) {

    }
}   // End of BasicSoul class