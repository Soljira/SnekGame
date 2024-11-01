package com.example.snekgame

import kotlinx.coroutines.Runnable

// Utilizes multithreading by implementing Runnable
class GameLoop(val gamePanel: GamePanel) : Runnable {

    val gameThread : Thread = Thread(this)

    override fun run() {
        // FPS checker; has nothing to do with the gameplay code
        var lastFPSCheck : Long = System.currentTimeMillis()
        var fps : Int = 0
        var lastDelta : Long = System.nanoTime()
        var nanoSec = 1_000_000_000

        while (true) {
            var nowDelta : Long = System.nanoTime()
            var timeSinceLastDelta : Double = nowDelta.toDouble() - lastDelta
            var delta : Double = timeSinceLastDelta / nanoSec


            gamePanel.update(delta)
            gamePanel.render()
            lastDelta = nowDelta

            fps++

            var now : Long = System.currentTimeMillis()
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

}