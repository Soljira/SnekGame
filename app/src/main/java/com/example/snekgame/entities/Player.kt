package com.example.snekgame.entities

import android.graphics.PointF
import com.example.snekgame.helpers.GameConstants
import com.example.snekgame.inputs.TouchEvents

class Player : Character(
    PointF(540F, 960F), // center position
    GameCharacters.PLAYER
) {

    fun update(delta : Double) {

        when (facingDirection) {
            // Checks if the player sprite reaches the screen boundaries. If yes, stops the player sprite
            // from moving out the screen
            GameConstants.Face_Direction.UP -> {
                if (position.y > GameConstants.Boundary.TOP) {
                    position = PointF(
                        position.x,
                        position.y - delta.toFloat() * GameConstants.Player.BASE_SPEED
                    )
                }
            }
            GameConstants.Face_Direction.DOWN -> {
                if (position.y < GameConstants.Boundary.BOTTOM) {
                    position = PointF(
                        position.x,
                        position.y + delta.toFloat() * GameConstants.Player.BASE_SPEED
                    )
                }
            }
            GameConstants.Face_Direction.LEFT -> {
                if (position.x > GameConstants.Boundary.LEFT) {
                    position = PointF(
                        position.x - delta.toFloat() * GameConstants.Player.BASE_SPEED,
                        position.y
                    )
                }
            }
            GameConstants.Face_Direction.RIGHT -> {
                if (position.x < GameConstants.Boundary.RIGHT) {
                    position = PointF(
                        position.x + delta.toFloat() * GameConstants.Player.BASE_SPEED,
                        position.y
                    )
                }
            }
        }
    }  // End of update function

    // Tracks the last dpad button touched and changes the player direction accordingly
    fun setDirection(direction: Int) {
        when (direction) {
            TouchEvents.UP -> facingDirection = GameConstants.Face_Direction.UP
            TouchEvents.DOWN -> facingDirection = GameConstants.Face_Direction.DOWN
            TouchEvents.LEFT -> facingDirection = GameConstants.Face_Direction.LEFT
            TouchEvents.RIGHT -> facingDirection = GameConstants.Face_Direction.RIGHT
        }
    }
}