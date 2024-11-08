package com.example.snekgame.entities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.snekgame.helpers.GameConstants
import com.example.snekgame.inputs.TouchEvents
import kotlin.math.pow
import kotlin.math.sqrt

/*
    THANK YOU FOR THE REFERENCE INDIAN KUYA
    https://visualandroidblog.blogspot.com/2023/03/create-simple-snake-game-in-android.html?m=1
 */

class Player : Character(
    PointF(540F, 500F), // center position
    GameCharacters.PLAYER
) {
    private var isPaused: Boolean = false
    private var previousDirection: Int = GameConstants.Face_Direction.NONE

    // List to store snake segments
    var segments = mutableListOf<PointF>()
    private val segmentSize = 70f // Size of each snake segment
    private var isGameOver = false

    // TODO: Change this to a sprite, pero 1 sprite lang para hindi na magpakahirap
    private val paint = Paint().apply {
        color = Color.GREEN
    }

    init {
        // Initialize ung snakeSegment list with one segment (the head)
        segments.add(PointF(position.x, position.y))
    }

    fun update(delta: Double) {
        if (isPaused || isGameOver) {
            return  // Don't update if paused or game over
        }

        val prevHeadPos = PointF(segments[0].x, segments[0].y)  // Store previous head position


        if (!isPaused) {
            when (facingDirection) {    // Updates head position based on direction
                // This is only useful for sprites. Hindi na useful sa square kasi square lang naman
                GameConstants.Face_Direction.UP -> {
                    if (segments[0].y > GameConstants.Boundary.TOP) {  // TODO: Change the Boundary enum
                        segments[0].y -= delta.toFloat() * GameConstants.Player.BASE_SPEED
                    } else {
                        handleCollision()
                    }
                }

                GameConstants.Face_Direction.DOWN -> {
                    if (segments[0].y < GameConstants.Boundary.BOTTOM) {
                        segments[0].y += delta.toFloat() * GameConstants.Player.BASE_SPEED
                    } else {
                        handleCollision()
                    }
                }

                GameConstants.Face_Direction.LEFT -> {
                    if (segments[0].x > GameConstants.Boundary.LEFT) {
                        segments[0].x -= delta.toFloat() * GameConstants.Player.BASE_SPEED
                    } else {
                        handleCollision()
                    }
                }

                GameConstants.Face_Direction.RIGHT -> {
                    if (segments[0].x < GameConstants.Boundary.RIGHT) {
                        segments[0].x += delta.toFloat() * GameConstants.Player.BASE_SPEED
                    } else {
                        handleCollision()
                    }
                }
            }
        }

        // Update position property to match head segment
        position = PointF(segments[0].x, segments[0].y)

        // Update following segments
        if (!isPaused) {
            for (i in 1 until segments.size) {
                val prevPos =
                    if (i == 1) prevHeadPos else PointF(segments[i - 1].x, segments[i - 1].y)
                val currentPos = segments[i]

                // Calculate direction to previous segment
                val dx = prevPos.x - currentPos.x
                val dy = prevPos.y - currentPos.y
                val distance = sqrt(dx.pow(2) + dy.pow(2))

                // Move segment towards previous segment if too far
                if (distance > segmentSize) {
                    val ratio = (distance - segmentSize) / distance
                    currentPos.x += dx * ratio
                    currentPos.y += dy * ratio
                }
            }

            checkSelfCollision()
        }
    }

    fun grow() {
        val lastSegment = segments.last()
        // Add new segment slightly behind the last segment
        segments.add(PointF(lastSegment.x - 10f, lastSegment.y - 10f))
    }

    private fun checkSelfCollision() {
        if (segments.size < 4) return // Need at least 4 segments for collision

        val head = segments[0]
        // Check collision with all segments except the first few
        // NO FUCKING IDEA WHAT THIS DOES. THANK YOU INDIANS
        for (i in 4 until segments.size) {
            val segment = segments[i]
            val distance = sqrt(
                (head.x - segment.x).pow(2) +
                        (head.y - segment.y).pow(2)
            )

            if (distance < segmentSize * 0.8f) { // Using 0.8 as collision threshold
                handleCollision()
                break
            }
        }
    }

    private fun handleCollision() {
        isGameOver = true
    }

    fun draw(canvas: Canvas) {
        // Draws all segments every time nagmomove player
        segments.forEach { segment ->
            canvas.drawRect(
                segment.x,
                segment.y,
                segment.x + segmentSize,
                segment.y + segmentSize,
                paint
            )
        }
    }

    fun isGameOver(): Boolean = isGameOver
    fun setGameOver(boolean : Boolean) {
        isGameOver = boolean
    }


    fun setDirection(direction: Int) {
        println("Player update - isPaused: $isPaused, isGameOver: $isGameOver")

        if (isPaused || isGameOver) {
            println("Player update - early return due to pause/gameover")
            return  // Don't update if paused or game over
        }

        when (direction) {
        // added a feature that prevents 180-degree turns
            TouchEvents.UP -> {
                if (facingDirection != GameConstants.Face_Direction.DOWN) {
                    facingDirection = GameConstants.Face_Direction.UP
                }
            }
            TouchEvents.DOWN -> {
                if (facingDirection != GameConstants.Face_Direction.UP) {
                    facingDirection = GameConstants.Face_Direction.DOWN
                }
            }
            TouchEvents.LEFT -> {
                if (facingDirection != GameConstants.Face_Direction.RIGHT) {
                    facingDirection = GameConstants.Face_Direction.LEFT
                }
            }
            TouchEvents.RIGHT -> {
                if (facingDirection != GameConstants.Face_Direction.LEFT) {
                    facingDirection = GameConstants.Face_Direction.RIGHT
                }
            }
        }
    }

    fun setPaused(paused: Boolean) {
        println("Setting player pause to: $paused")
        isPaused = paused
        if (paused) {
            previousDirection = facingDirection
            facingDirection = GameConstants.Face_Direction.NONE
        }
//        TODO: No idea why the snake jumps after unpausing
//        } else {
//            facingDirection = previousDirection
//        }
    }

    fun reset() {
        resetPosition()
        resetAnimation()
        facingDirection = GameConstants.Face_Direction.DOWN
    }
}