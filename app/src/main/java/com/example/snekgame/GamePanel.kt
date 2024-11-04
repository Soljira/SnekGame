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

import com.example.snekgame.entities.Character
import com.example.snekgame.entities.Player
import com.example.snekgame.entities.souls.BasicSoul

import com.example.snekgame.inputs.TouchEvents
import com.example.snekgame.environments.GameMap
import java.util.Random

// TODO: Animate the snake and the souls
// TODO: Put snake player in an ArrayList

// : -> extends
// super(context) is no longer needed as Kotlin automatically does that in SurfaceView(context)
class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val holder = getHolder()
    private val random = Random()
    private var gameLoop = GameLoop(this)
    private var touchEvents: TouchEvents
    private var dpadLastDirection = TouchEvents.NONE  // For tracking the last DPAD button pressed by the user
    private var score = 0  // Track player score

    // TODO: Array for snake head, snake segments, and snake tail. maybe 2d array

    // Testing Map
    private val testMap : GameMap

    private val player : Player
    private val basicSoul : BasicSoul

    private val paint = Paint().apply {
        color = Color.GREEN  // dark green
    }

    init {
        holder.addCallback(this)
        gameLoop = GameLoop(this)
        touchEvents = TouchEvents(this)

        player = Player()     // center position
        basicSoul = BasicSoul(PointF(random.nextInt(980).toFloat(), random.nextInt(1242).toFloat()))

        // See reference map.png in assets folder to see what the map looks like
        // 12 x 14 map (16x16 pixels resolution)
        val spriteIds = arrayOf(
            intArrayOf(254, 192, 210, 210, 210, 210, 210, 210, 210, 193, 253, 188),
            intArrayOf(192, 211, 275, 278, 276, 275, 276, 275, 279, 209, 193, 188),
            intArrayOf(189, 278, 169, 171, 166, 167, 279, 275, 278, 276, 187, 188),
            intArrayOf(189, 275, 190, 187, 254, 189, 276, 275, 278, 279, 187, 188),
            intArrayOf(189, 276, 190, 209, 210, 211, 165, 166, 166, 167, 187, 188),
            intArrayOf(189, 278, 212, 275, 279, 275, 187, 188, 253, 189, 187, 188),
            intArrayOf(194, 279, 275, 278, 276, 165, 215, 188, 254, 189, 187, 188),
            intArrayOf(216, 278, 275, 275, 276, 187, 188, 188, 188, 189, 187, 188),
            intArrayOf(189, 275, 165, 166, 167, 187, 254, 188, 188, 189, 187, 188),
            intArrayOf(189, 276, 187, 253, 189, 209, 210, 210, 210, 211, 187, 188),
            intArrayOf(189, 278, 209, 210, 211, 278, 275, 275, 279, 276, 187, 188),
            intArrayOf(174, 167, 275, 278, 276, 275, 276, 275, 278, 165, 196, 210),
            intArrayOf(215, 214, 166, 166, 166, 166, 166, 166, 166, 215, 211, 278)
        )

        // Initialize the GameMap with the provided sprite IDs
        testMap = GameMap(spriteIds)
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

        testMap.draw(canvas)    // Paints the canvas with the map

        //Score top left
        canvas.drawText("Score: $score", 50f, 100f, scorePaint)

        drawPlayer(canvas)
        drawCharacter(canvas, basicSoul)

        touchEvents.draw(canvas)

        holder.unlockCanvasAndPost(canvas)  // Displays the canvas
    }

    private fun drawPlayer(canvas: Canvas) {
//        canvas.drawBitmap(
//            player.gameCharacterType.getSprite(player.animationIndex, player.facingDirection)!!,
//            player.hitbox.left,
//            player.hitbox.top,
//            null)
        canvas.drawRect(
            player.position.x,
            player.position.y,
            player.position.x + 70f,  // Set the width of the square
            player.position.y + 70f,  // Set the height of the square
            paint
        )
    }

    private fun drawCharacter(canvas: Canvas, character: Character) {
        canvas.drawBitmap(
            character.gameCharacterType.getSprite(character.animationIndex, character.facingDirection)!!,
            character.hitbox.left,
            character.hitbox.top,
            null)

    }

    fun update(delta : Double) {
        updatePlayerMove(delta)
        basicSoul.update(delta)
        checkSoulCollision()
    }  // End of update function

    fun updatePlayerMove(delta: Double) {
        player.setDirection(dpadLastDirection)
        player.update(delta)
    }

    private fun checkSoulCollision() {
        if (player.position.distance(basicSoul.position) < 50) {  // If player is close to the soul
            score++  // Increase score
            basicSoul.position = PointF(random.nextInt(980).toFloat(), random.nextInt(1242).toFloat())  // Move soul to new random position
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Only adds the snake to the canvas if the user is pressing down
        when (event?.action) {
            // when user is pressing down the button
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                dpadLastDirection = touchEvents.handleTouch(event.x, event.y, true)
            }
            // when user releases the button
            MotionEvent.ACTION_UP -> {
                dpadLastDirection = touchEvents.handleTouch(event.x, event.y, false)
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