/**
 * Handles the buttons using SurfaceView, which apparently uses multithreading to separate input from gameplay
 * Tutorial: https://www.youtube.com/watch?v=QV1uCncZ2vc&list=PL4rzdwizLaxYqSDifOi7mUtyzdM6R_2sT&index=2
 */
package com.example.snekgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.VectorDrawable
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import android.media.MediaPlayer

import com.example.snekgame.entities.Character
import com.example.snekgame.entities.Player
import com.example.snekgame.entities.souls.BasicSoul


import com.example.snekgame.inputs.TouchEvents
import com.example.snekgame.environments.GameMap
import com.example.snekgame.helpers.GameConstants
import com.example.snekgame.helpers.SoundEffectsConstants
import java.util.Random

// TODO: Animate the snake and the souls
// TODO: THIS CODE SUCKS; PLEASE FIX IT AFTER PASSING
// : -> extends
// super(context) is no longer needed as Kotlin automatically does that in SurfaceView(context)
class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val holder = getHolder()
    private val random = Random()
    private var gameLoop = GameLoop(this)
    private var touchEvents: TouchEvents
    private var dpadLastDirection = TouchEvents.NONE  // For tracking the last DPAD button pressed by the user
    private var score = 0  // Track player score
    private var isPaused: Boolean = false

    // TODO: Add switch cases for changing dimensions based on device (cursed mano-mano way)

    // Testing Map
    private val testMap : GameMap

    // UI elements
    private val gameFrame = ContextCompat.getDrawable(context, R.drawable.game_frame) as VectorDrawable
    // TODO: @GAB @LEEIAN NANDITO PAUSE BUTTON LAGYAN KO MAMAYA NG IMAGE PERO YAN UNG BUTTON
    private val pauseButtonBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pause)
    private val pauseButtonRect = Rect(50, 50, 150, 150) // Define the pause button's position and size

    private val backgroundBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.dungeon)
    private val scaledBackground = Bitmap.createScaledBitmap(
        backgroundBitmap,
        GameConstants.Frame.WIDTH * 5,  // Width of the canvas
        GameConstants.Frame.HEIGHT * 2,  // Height of the canvas
        false
    )
    private val dpadBackgroundBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.dpad_background)
    private val scaledDpadBackground = Bitmap.createScaledBitmap(
        dpadBackgroundBitmap,
        GameConstants.DpadBackground.WIDTH * 2,  // Width of the canvas
        GameConstants.DpadBackground.HEIGHT * 2,  // Height of the canvas
        false
    )

    private val gameOverScreen : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.game_over_screen)
    private lateinit var gameOverScreenRect: Rect
    private val retryButton : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.button_retry)
    private val scaledRetryButton = Bitmap.createScaledBitmap(
        retryButton,
        70*2,
        70*2,
        false
    )

    private var retryButtonRect = Rect(400, 1800, 680, 1950)

    // Sound elements
    private lateinit var eatSound: MediaPlayer
    private lateinit var deathSound: MediaPlayer

    private val player : Player
    private val basicSoul : BasicSoul

    init {
        holder.addCallback(this)
        gameLoop = GameLoop(this)
        touchEvents = TouchEvents(this)

        player = Player()     // center position
        // TODO: MAKE 150 AND 410 CONSTANTS IN GAMECONSTANTS PLEASE T_T
        basicSoul = BasicSoul(PointF(
            (150 + random.nextInt(GameConstants.Boundary.RIGHT - 150 + 1)).toFloat(),  // no idea why this works
            (410 + random.nextInt(GameConstants.Boundary.BOTTOM - 410 + 1)).toFloat()  // basta may range ung random; 150 is supposed to be the screen boundary
        ))

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

        eatSound = MediaPlayer.create(context, SoundEffectsConstants.EAT)
        deathSound = MediaPlayer.create(context, SoundEffectsConstants.DEATH)
    }

    private val scorePaint = Paint().apply {
        color = Color.WHITE   // Set text color
        textSize = 60f        // Set text size
        isAntiAlias = true    // Enable anti-aliasing for smoother text
    }

    fun render() {
        val canvas : Canvas = holder.lockCanvas()  // Prepares the canvas for drawing

        // Background
//        canvas.drawColor(Color.BLACK)  // Resets the canvas to a black bg whenever the user touches the screen
//        canvas.drawBitmap()
//        canvas.drawBitmap(scaledBackground, 0f, 1350f, null)  // Draw at the top-left corner (0, 0)

        canvas.drawBitmap(scaledBackground, 0f, 0f, null)  // Draw at the top-left corner (0, 0)

        // What the fuck are screen dimensions
        val xPosDpad = (canvas.width - scaledDpadBackground.width) / 2f
        canvas.drawBitmap(scaledDpadBackground, xPosDpad, 2150f, null)  // BG for the controls UI

//        canvas.drawBitmap(scaledDpadBackground, 380f, 2150f, null)

        testMap.draw(canvas)    // Paints the canvas with the map

        /*
            UI stuff
         */
        // TODO: FIX THIS. use canvas.drawchuchu instead!
        gameFrame.setBounds(0, 270, GameConstants.Frame.WIDTH, GameConstants.Frame.HEIGHT) // x, y, width, height
        gameFrame.draw(canvas)

//        canvas.drawBitmap(gameOverScreen, null, gameOverScreenRect, null)

//        dpadBackground.setBounds(0, 700, GameConstants.DpadBackground.WIDTH, GameConstants.DpadBackground.HEIGHT) // x, y, width, height

        //Score top left
        val textWidth = scorePaint.measureText("Score")  // VERY BAD CODE T_T
        // Calculate the x-coordinate for centered text
        val xPosScore = (canvas.width - textWidth) / 2  // Centers the text horizontally
        canvas.drawText("Score: $score", xPosScore, 2060f, scorePaint)

        // pausebtn
        canvas.drawBitmap(pauseButtonBitmap, null, pauseButtonRect, null)

        drawPlayer(canvas)
        drawCharacter(canvas, basicSoul)
        touchEvents.draw(canvas)

        drawGameOverScreen(canvas)

        holder.unlockCanvasAndPost(canvas)  // Displays the canvas
    }

    private fun drawPlayer(canvas: Canvas) {
//        canvas.drawBitmap(
//            player.gameCharacterType.getSprite(player.animationIndex, player.facingDirection)!!,
//            player.hitbox.left,
//            player.hitbox.top,
//            null)
//        canvas.drawRect(
//            player.position.x,
//            player.position.y,
//            player.position.x + 70f,  // Set the width of the square
//            player.position.y + 70f,  // Set the height of the square
//            paint
//        )
        // The code above isn't needed kasi nasa Player class na
        // Speaking of other classes handling relevant code
        // TODO: Clean up GamePanel. Follow https://www.youtube.com/watch?v=UgOhE7TCc-o&list=PL4rzdwizLaxYqSDifOi7mUtyzdM6R_2sT&index=13 to organize code
        player.draw(canvas)
    }

    private fun drawCharacter(canvas: Canvas, character: Character) {
        canvas.drawBitmap(
            character.gameCharacterType.getSprite(character.animationIndex, character.facingDirection)!!,
            character.hitbox.left,
            character.hitbox.top,
            null)

    }  // End of drawCharacter function

    fun update(delta: Double) {
        println("GamePanel update - isPaused: $isPaused")
        if (isPaused) {
            println("GamePanel update - returning due to pause")
            return  // Don't do any updates if paused
        }

        if (!player.isGameOver()) {
            updatePlayerMove(delta)
            basicSoul.update(delta)
            checkSoulCollision()
            if (player.isGameOver()) {
                deathSound.start()
            }
        }
    }  // End of update function

    // Toggle the game's pause state
    private fun togglePause() {
        println("Before toggle - isPaused: $isPaused")

        isPaused = !isPaused
        println("New pause state: $isPaused")
        player.setPaused(isPaused)  // Update player's pause state
        gameLoop.setPaused(isPaused)
        if (isPaused) {
            dpadLastDirection = TouchEvents.NONE  // Reset direction when paused
        }

        println("After toggle - isPaused: $isPaused")
    }

    fun updatePlayerMove(delta: Double) {
        println("updatePlayerMove - isPaused: $isPaused")
        if (isPaused) {
            println("updatePlayerMove - returning due to pause")
            return
        }
        player.setDirection(dpadLastDirection)
        player.update(delta)
    }

    private fun checkSoulCollision() {
        if (player.position.distance(basicSoul.position) < 100) {
            score++  // Increases score
            player.grow() // Adds a snake segment every time the player collects/eats a soul
//            TODO: FIX THIS!
            basicSoul.position = PointF(
                (150 + random.nextInt(GameConstants.Boundary.RIGHT - 150 + 1)).toFloat(),  // no idea why this works
                (410 + random.nextInt(GameConstants.Boundary.BOTTOM - 410 + 1)).toFloat()  // basta may range ung random; 80 is supposed to be the screen boundary
            )
            eatSound.start()
        }
    }  // End of checkSoulCollision function
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                if (player.isGameOver() && retryButtonRect.contains(event.x.toInt(), event.y.toInt())) {
//                    // Reset the game when retry is pressed
//                    resetGame()
//                    return true
//                }
//                if (pauseButtonRect.contains(event.x.toInt(), event.y.toInt())) {
//                    togglePause()
//                    return true
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (!isPaused && !player.isGameOver()) {
//                    dpadLastDirection = touchEvents.handleTouch(event.x, event.y, true)
//                }
//            }
//            MotionEvent.ACTION_DOWN -> {
//                if (pauseButtonRect.contains(event.x.toInt(), event.y.toInt())) {
//                    println("Pause button pressed")
//                    togglePause()
//                    return true
//                }
//            }
//            // when user is pressing down the button
//            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
//                // Only process movement if not paused
//                if (!isPaused && !player.isGameOver()) {
//                    dpadLastDirection = touchEvents.handleTouch(event.x, event.y, true)
//                }
//            }
//            // when user releases the button
//            MotionEvent.ACTION_UP -> {
//                // Only process movement if not paused
//                if (!isPaused && !player.isGameOver()) {
//                    dpadLastDirection = touchEvents.handleTouch(event.x, event.y, false)
//                }
//            }
//        }
//        return true
//    }

    // TODO: Clean these and move these to a separate class because it's ugly
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (player.isGameOver() && isRetryButtonPressed(event)) {
                    resetGame()
                    return true
                } else if (isPauseButtonPressed(event)) {
                    togglePause()
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isPaused && !player.isGameOver()) {
                    dpadLastDirection = touchEvents.handleTouch(event.x, event.y, true)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isPaused && !player.isGameOver()) {
                    dpadLastDirection = touchEvents.handleTouch(event.x, event.y, false)
                }
            }
        }
        return true
    }
//    private fun handleActionDown(event: MotionEvent) {
//        if (player.isGameOver() && isRetryButtonPressed(event)) {
//            resetGame()
//        } else if (isPauseButtonPressed(event)) {
//            togglePause()
//        }
//    }
//
//    private fun handleActionMove(event: MotionEvent) {
//        if (!isPaused && !player.isGameOver()) {
//            dpadLastDirection = touchEvents.handleTouch(event.x, event.y, true)
//        }
//    }
//
//    private fun handleActionUp(event: MotionEvent) {
//        if (!isPaused && !player.isGameOver()) {
//            dpadLastDirection = touchEvents.handleTouch(event.x, event.y, false)
//        }
//    }
//
    private fun isRetryButtonPressed(event: MotionEvent): Boolean {
        return retryButtonRect.contains(event.x.toInt(), event.y.toInt())
    }

    private fun isPauseButtonPressed(event: MotionEvent): Boolean {
        return pauseButtonRect.contains(event.x.toInt(), event.y.toInt())
    }

    // Game over condition and score reset
    fun drawGameOverScreen(canvas: Canvas) {
        if (player.isGameOver()) {
            // Center the game over screen bitmap
            val left = (canvas.width - gameOverScreen.width) / 2
            val top = (canvas.height - gameOverScreen.height) / 3 // Move the game over screen higher
            canvas.drawBitmap(gameOverScreen, left.toFloat(), top.toFloat(), null)

            // Draw the retry button
            val retryButtonWidth = 70*2
            val retryButtonHeight = 70*2
            val retryButtonLeft = (canvas.width - retryButtonWidth) / 2
            val retryButtonTop = top.toInt() + gameOverScreen.height + 100 // Position below the game over screen

            retryButtonRect = Rect(
                retryButtonLeft,
                retryButtonTop,
                retryButtonLeft + retryButtonWidth,
                retryButtonTop + retryButtonHeight
            )
            canvas.drawBitmap(scaledRetryButton, retryButtonLeft.toFloat(), retryButtonTop.toFloat(), null)
        } else {
            // Reset the game over screen and retry button
            gameOverScreenRect = Rect(0, 0, 0, 0)
            retryButtonRect = Rect(0, 0, 0, 0)
        }
    }

    // Reset the game state
    fun resetGame() {
        score = 0 // Reset player score
        player.reset() // Reset the player position and other properties
        player.segments.clear() // Clear the snake segments
        player.segments.add(PointF(player.position.x, player.position.y)) // Add the initial segment
        basicSoul.reset() // Reset the basicSoul object
        isPaused = false // Unpause the game if it was paused
        player.setGameOver(false) // Reset the game over state using the setter method
        player.resetAnimation() // Resume the player's animation
        basicSoul.resetAnimation() // Resume the soul's animation
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