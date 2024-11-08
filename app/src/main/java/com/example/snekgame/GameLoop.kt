package com.example.snekgame

import kotlinx.coroutines.Runnable

// Utilizes multithreading by implementing Runnable
class GameLoop(val gamePanel: GamePanel) : Runnable {

    val gameThread : Thread = Thread(this)
    private var isPaused: Boolean = false //track pause state


    override fun run() {
        // FPS checker; has nothing to do with the gameplay code
        var lastFPSCheck : Long = System.currentTimeMillis()
        var fps : Int = 0
        var lastDelta : Long = System.nanoTime()
        var nanoSec = 1_000_000_000

        while (true) {
            println("GameLoop - isPaused: $isPaused")
            if (isPaused) {
                println("GameLoop - skipping update due to pause")
                // When paused, sleep briefly to prevent busy-waiting
                Thread.sleep(100)
                continue
            }

            var nowDelta: Long = System.nanoTime()
            var timeSinceLastDelta: Double = nowDelta.toDouble() - lastDelta
            var delta: Double = timeSinceLastDelta / nanoSec


            gamePanel.update(delta)
            gamePanel.render()
            lastDelta = nowDelta

            fps++

            var now: Long = System.currentTimeMillis()
            if (now - lastFPSCheck >= 1000) {
                println("FPS: $fps ${System.currentTimeMillis()}")
                fps = 0
                lastFPSCheck += 1000
            }

        }
    }

    fun startGameLoop() {
        gameThread.start()
    }

    //Pause state
    fun setPaused(paused: Boolean){
        println("Setting GameLoop pause to: $paused")
        isPaused = paused
    }

}