/**
 * Handles the buttons using SurfaceView, which apparently uses multithreading to separate input from gameplay
 * Tutorial: https://www.youtube.com/watch?v=QV1uCncZ2vc&list=PL4rzdwizLaxYqSDifOi7mUtyzdM6R_2sT&index=2
 */
package com.example.snekgame

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
import com.example.snekgame.inputs.TouchEvents
import java.util.Random


// TODO: Animate the snake and the souls
// TODO: Put snake player in an ArrayList
// TODO: Whenever player touches a soul, increase the score count by 1 and add a new soul to the screen, removing the eaten soul

// : -> extends
// super(context) is no longer needed as Kotlin automatically does that in SurfaceView(context)
class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val redPaint = Paint()
    private val holder = getHolder()
    private val random = Random()
    private var gameLoop = GameLoop(this)
    private var touchEvents: TouchEvents

    // Old variables for tracking player sprite position; now using playerPos
    private var x : Float = 0F
    private var y : Float = 0F

    // Player position coordinates
    private var playerPos : PointF = PointF(540F, 960F)  // center position

    private var soulPos : PointF = PointF(0F, 0F)
    private var soulDirection = GameConstants.Face_Direction.DOWN

    private var lastDirectionChange : Long = System.currentTimeMillis()

    // address for the spritesheet
    private var playerAnimateIndexY = 0
    private var playerFaceDir = GameConstants.Face_Direction.RIGHT

    private var animationTick = 0
    private var animationSpeed = 10
    private var currentDirection = TouchEvents.NONE  // For tracking the last DPAD button pressed by the user
    private var score = 0  // Track player score
    init {
        holder.addCallback(this)
        redPaint.color = Color.RED
        gameLoop = GameLoop(this)
        touchEvents = TouchEvents(this)

        soulPos = PointF(random.nextInt(1080).toFloat(), random.nextInt(1920).toFloat())
    }
    private val scorePaint = Paint().apply {
        color = Color.RED   // Set text color
        textSize = 60f        // Set text size
        isAntiAlias = true    // Enable anti-aliasing for smoother text
    }

    fun render() {
        val canvas : Canvas = holder.lockCanvas()  // Prepares the canvas for drawing

        // Background
        canvas.drawColor(Color.BLACK)  // Resets the canvas to a black bg whenever the user touches the screen
        //Score top left
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)
        // Draws the player character to the canvas
        canvas.drawBitmap(GameCharacters.PLAYER.getSprite(playerAnimateIndexY, playerFaceDir)!!, playerPos.x, playerPos.y, null)


        // Draws the soul to the canvas
        canvas.drawBitmap(GameCharacters.SOUL.getSprite(playerAnimateIndexY, soulDirection)!!, soulPos.x, soulPos.y, null)

        touchEvents.draw(canvas)

        holder.unlockCanvasAndPost(canvas)  // Displays the canvas
    }

    fun update(delta : Double) {

        // When the user presses the DPAD button
        // TODO: Rename currentDirection to a more sensible one
        when (currentDirection) {
            TouchEvents.UP -> playerFaceDir = GameConstants.Face_Direction.UP
            TouchEvents.DOWN -> playerFaceDir = GameConstants.Face_Direction.DOWN
            TouchEvents.LEFT -> playerFaceDir = GameConstants.Face_Direction.LEFT
            TouchEvents.RIGHT -> playerFaceDir = GameConstants.Face_Direction.RIGHT
        }  // End of when block

        when (playerFaceDir) {
            GameConstants.Face_Direction.UP -> {
                // Checks if the player sprite reaches the screen boundaries. If yes, stops the player sprite
                // from moving out the screen
                if (playerPos.y <= 0)
                    playerPos.y -= 0
                else
                    playerPos.y -= delta.toFloat() * 500
            }
            GameConstants.Face_Direction.DOWN -> {
                if (playerPos.y >= 1520)
                    playerPos.y += 0
                else
                    playerPos.y += delta.toFloat() * 500
            }

            GameConstants.Face_Direction.LEFT -> {
                if (playerPos.x <= 0)
                    playerPos.x -= 0
                else
                    playerPos.x -= delta.toFloat() * 500
            }
            GameConstants.Face_Direction.RIGHT -> {
                if (playerPos.x >= 980)
                    playerPos.x += 0
                else
                    playerPos.x += delta.toFloat() * 500
            }
        }

        updateAnimation()
        checkSoulCollision()
    }  // End of update function

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
    private fun checkSoulCollision() {
        if (playerPos.distance(soulPos) < 50) {  // If player is close to the soul
            score++  // Increase score
            soulPos = PointF(random.nextInt(1080).toFloat(), random.nextInt(1920).toFloat())  // Move soul to new random position
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Only adds the snake to the canvas if the user is pressing down
        when (event?.action) {
            // when user is pressing down the button
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                currentDirection = touchEvents.handleTouch(event.x, event.y, true)
            }
            // when user releases the button
            MotionEvent.ACTION_UP -> {
                currentDirection = touchEvents.handleTouch(event.x, event.y, false)
            }
        }
        return true
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        gameLoop.startGameLoop()

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {

    }
}

// Extension function to calculate the distance between two points
fun PointF.distance(other: PointF): Float {
    return kotlin.math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y))
}