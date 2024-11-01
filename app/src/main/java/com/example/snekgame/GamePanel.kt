/**
 * Handles the buttons using SurfaceView, which apparently uses multithreading to separate input from gameplay
 * Tutorial: https://www.youtube.com/watch?v=QV1uCncZ2vc&list=PL4rzdwizLaxYqSDifOi7mUtyzdM6R_2sT&index=2
 */
package com.example.snekgame;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.snekgame.entities.GameCharacters
import com.example.snekgame.helpers.GameConstants
import java.util.Random
import java.util.ArrayList

// TODO: Animate the snake and the souls
// TODO: Put snake player in an ArrayList

// : -> extends
// super(context) is no longer needed as Kotlin automatically does that in SurfaceView(context)
class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val redPaint = Paint()
    private val holder = getHolder()
    private val random = Random()
    private var gameLoop = GameLoop(this)
    private var x : Float = 0F
    private var y : Float = 0F

    private var soulPos : PointF = PointF(0F, 0F)
    private var soulDirection = GameConstants.Face_Direction.DOWN

    private var lastDirectionChange : Long = System.currentTimeMillis()

    // address for the spritesheet
    private var playerAnimateIndexY = 0
    private var playerFaceDir = GameConstants.Face_Direction.RIGHT

    private var animationTick = 0
    private var animationSpeed = 10

    init {
        holder.addCallback(this)
        redPaint.color = Color.RED
        gameLoop = GameLoop(this)

        soulPos = PointF(random.nextInt(1080).toFloat(), random.nextInt(1920).toFloat())

    }

    fun render() {
        val canvas : Canvas = holder.lockCanvas()  // Prepares the canvas for drawing

        // Background
        canvas.drawColor(Color.BLACK)  // Resets the canvas to a black bg whenever the user touches the screen

        // Draws the player character to the canvas
        canvas.drawBitmap(GameCharacters.PLAYER.getSprite(playerAnimateIndexY, playerFaceDir)!!, x, y, null)


        // Draws the soul to the canvas
        canvas.drawBitmap(GameCharacters.SOUL.getSprite(playerAnimateIndexY, soulDirection)!!, soulPos.x, soulPos.y, null)

        holder.unlockCanvasAndPost(canvas)  // Displays the canvas
    }

    fun update(delta : Double) {

        updateAnimation()

    }

    fun updateAnimation() {
        animationTick++

        // When animationTick reaches animationSpeed, reset it to 0 and increment playerAnimateIndexY
        if (animationTick >= animationSpeed) {
            animationTick = 0
            playerAnimateIndexY++  // Moves to the next animation frame

            // Resets the animation loop
            if (playerAnimateIndexY >= 4)
                playerAnimateIndexY = 0
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Only adds the snake to the canvas if the user is pressing down
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            // Makes it so that the snake player faces the direction where the user finger is
            var newX : Float = event.x
            var newY : Float = event.y

            var xDifference : Float = kotlin.math.abs(newX - x)
            var yDifference : Float = kotlin.math.abs(newY - y)

            playerFaceDir = if (xDifference > yDifference) {
                if (newX > x) GameConstants.Face_Direction.RIGHT else GameConstants.Face_Direction.LEFT
            } else {
                if (newY > y) GameConstants.Face_Direction.DOWN else GameConstants.Face_Direction.UP
            }

            x = newX
            y = newY
        }
        return true
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        gameLoop.startGameLoop()

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Not implemented in the original Java code
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        // Not implemented in the original Java code
    }
}