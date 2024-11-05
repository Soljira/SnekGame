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
import android.graphics.drawable.VectorDrawable
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat

import com.example.snekgame.entities.Character
import com.example.snekgame.entities.Player
import com.example.snekgame.entities.souls.BasicSoul

import com.example.snekgame.inputs.TouchEvents
import com.example.snekgame.environments.GameMap
import com.example.snekgame.helpers.GameConstants
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

    // TODO: Add switch cases for changing dimensions based on device (cursed mano-mano way)

    // Testing Map
    private val testMap : GameMap

    // UI elements
    private val gameFrame = ContextCompat.getDrawable(context, R.drawable.game_frame) as VectorDrawable
    // TODO: @GAB @LEEIAN NANDITO PAUSE BUTTON LAGYAN KO MAMAYA NG IMAGE PERO YAN UNG BUTTON
//    private val pauseButton = ContextCompat.getDrawable(context, R.drawable.game_frame) as VectorDrawable
    private val backgroundBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.dungeon)
    private val scaledBackground = Bitmap.createScaledBitmap(
        backgroundBitmap,
        GameConstants.Frame.WIDTH * 3,  // Width of the canvas
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

    private val player : Player
    private val basicSoul : BasicSoul

    init {
        holder.addCallback(this)
        gameLoop = GameLoop(this)
        touchEvents = TouchEvents(this)

        player = Player()     // center position
        basicSoul = BasicSoul(PointF(
            (80 + random.nextInt(GameConstants.Boundary.RIGHT - 80 + 1)).toFloat(),  // x between 80 and 933
            (80 + random.nextInt(GameConstants.Boundary.BOTTOM - 80 + 1)).toFloat()  // y between 80 and 1170
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
    }

    private val scorePaint = Paint().apply {
        color = Color.RED   // Set text color
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
        canvas.drawBitmap(scaledDpadBackground, 380f, 2130f, null)  // Draw behind the controls UI

        testMap.draw(canvas)    // Paints the canvas with the map

        /*
            UI stuff
         */
        // TODO: FIX THIS. use canvas.drawchuchu instead!
        gameFrame.setBounds(0, 270, GameConstants.Frame.WIDTH, GameConstants.Frame.HEIGHT) // x, y, width, height
        gameFrame.draw(canvas)

//        dpadBackground.setBounds(0, 700, GameConstants.DpadBackground.WIDTH, GameConstants.DpadBackground.HEIGHT) // x, y, width, height

        //Score top left
        canvas.drawText("Score: $score", 50f, 1450f, scorePaint)

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
        if (!player.isGameOver()) {
            updatePlayerMove(delta)
            basicSoul.update(delta)
            checkSoulCollision()
        }
    }  // End of update function

    fun updatePlayerMove(delta: Double) {
        player.setDirection(dpadLastDirection)
        player.update(delta)
    }

    private fun checkSoulCollision() {
        if (player.position.distance(basicSoul.position) < 50) {
            score++  // Increases score
            player.grow() // Adds a snake segment every time the player collects/eats a soul
            basicSoul.position = PointF(
                (410 + random.nextInt(GameConstants.Boundary.RIGHT - 410 + 1)).toFloat(),  // no idea why this works
                (110 + random.nextInt(GameConstants.Boundary.BOTTOM - 110 + 1)).toFloat()  // basta may range ung random; 80 is supposed to be the screen boundary
            )
        }
    }  // End of checkSoulCollision function

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
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