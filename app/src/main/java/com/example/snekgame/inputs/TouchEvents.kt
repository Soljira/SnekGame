package com.example.snekgame.inputs

/**
 * Handles all user inputs without cluttering GamePanel
 */

import android.graphics.Canvas
import android.graphics.Rect
import com.example.snekgame.GamePanel
import com.example.snekgame.ui.ButtonImages


class TouchEvents(gamePanel: GamePanel) {
    private val buttonWidth = 150
    private val buttonHeight = 150
    private val padding = 20

    // Button rectangles for collision detection
    private val upButton = Rect()
    private val downButton = Rect()
    private val leftButton = Rect()
    private val rightButton = Rect()

    // Initialize ButtonImages
    private val buttonImages = ButtonImages(gamePanel.context)

    // Track button states
    var isUpPressed = false
    var isDownPressed = false
    var isLeftPressed = false
    var isRightPressed = false

    fun draw(canvas: Canvas) {
        val screenWidth = canvas.width
        val screenHeight = canvas.height

        // Calculate button positions
        val centerX = screenWidth / 2 - buttonWidth / 2
        val centerY = screenHeight - (buttonHeight * 2.8F).toInt()

        // Set button boundaries for touch detection
        upButton.set(
            centerX,
            centerY - buttonHeight - padding,
            centerX + buttonWidth,
            centerY
        )

        downButton.set(
            centerX,
            centerY + buttonHeight + padding,
            centerX + buttonWidth,
            centerY + buttonHeight * 2 + padding
        )

        leftButton.set(
            centerX - buttonWidth - padding,
            centerY,
            centerX,
            centerY + buttonHeight
        )

        rightButton.set(
            centerX + buttonWidth + padding,
            centerY,
            centerX + buttonWidth * 2 + padding,
            centerY + buttonHeight
        )

        // Draw buttons using SVG drawables
        buttonImages.up?.let { drawable ->
            if (isUpPressed) {
                buttonImages.upPressed?.setBounds(upButton)
                buttonImages.upPressed?.draw(canvas)
            } else {
                drawable.setBounds(upButton)
                drawable.draw(canvas)
            }
        }

        buttonImages.down?.let { drawable ->
            if (isDownPressed) {
                buttonImages.downPressed?.setBounds(downButton)
                buttonImages.downPressed?.draw(canvas)
            } else {
                drawable.setBounds(downButton)
                drawable.draw(canvas)
            }
        }

        buttonImages.left?.let { drawable ->
            if (isLeftPressed) {
                buttonImages.leftPressed?.setBounds(leftButton)
                buttonImages.leftPressed?.draw(canvas)
            } else {
                drawable.setBounds(leftButton)
                drawable.draw(canvas)
            }
        }

        buttonImages.right?.let { drawable ->
            if (isRightPressed) {
                buttonImages.rightPressed?.setBounds(rightButton)
                buttonImages.rightPressed?.draw(canvas)
            } else {
                drawable.setBounds(rightButton)
                drawable.draw(canvas)
            }
        }
    }

    fun handleTouch(x: Float, y: Float, isPressed: Boolean): Int {
        if (isPressed) {
            when {
                upButton.contains(x.toInt(), y.toInt()) -> {
                    isUpPressed = true
                    isDownPressed = false
                    isLeftPressed = false
                    isRightPressed = false
                    return UP
                }
                downButton.contains(x.toInt(), y.toInt()) -> {
                    isUpPressed = false
                    isDownPressed = true
                    isLeftPressed = false
                    isRightPressed = false
                    return DOWN
                }
                leftButton.contains(x.toInt(), y.toInt()) -> {
                    isUpPressed = false
                    isDownPressed = false
                    isLeftPressed = true
                    isRightPressed = false
                    return LEFT
                }
                rightButton.contains(x.toInt(), y.toInt()) -> {
                    isUpPressed = false
                    isDownPressed = false
                    isLeftPressed = false
                    isRightPressed = true
                    return RIGHT
                }
                else -> {
                    // Touch is outside any button boundaries; reset all button states
                    isUpPressed = false
                    isDownPressed = false
                    isLeftPressed = false
                    isRightPressed = false
                }
            }
        } else {
            // If touch is released, reset all button states
            isUpPressed = false
            isDownPressed = false
            isLeftPressed = false
            isRightPressed = false
        }
        return NONE
    }

    companion object {
        const val NONE = -1
        const val UP = 0
        const val DOWN = 1
        const val LEFT = 2
        const val RIGHT = 3
    }
}