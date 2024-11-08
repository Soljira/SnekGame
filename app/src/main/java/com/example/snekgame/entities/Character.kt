package com.example.snekgame.entities

import android.graphics.PointF
import com.example.snekgame.helpers.GameConstants

/**
 * Superclass of all playable and non-playable characters in-game
 */

abstract class Character(
    position : PointF,
    gameCharacterType :
    GameCharacters) : Entity(position, 1F, 1F) {

    val gameCharacterType : GameCharacters = gameCharacterType

    protected var animationTick = 0

    // address for the spritesheet
    var animationIndex = 0
    var facingDirection = GameConstants.Face_Direction.DOWN

    private val initialPosition: PointF = PointF(position.x, position.y)

    protected fun updateAnimation() {
        animationTick++

        // When animationTick reaches animationSpeed, reset it to 0 and increment animationTick
        if (animationTick >= GameConstants.Animation.SPEED) {
            animationTick = 0
            animationIndex++  // Moves to the next animation frame

            // Resets the animation loop
            if (animationIndex >= GameConstants.Animation.AMOUNT )
                animationIndex = 0
        }
    }  // End of updateAnimation function

    fun resetAnimation() {
        animationTick = 0
        animationIndex = 0
    }

    fun resetPosition() {
        // Resets the character to the starting position
        this@Character.position.x = initialPosition.x
        this@Character.position.y = initialPosition.y
    }


}