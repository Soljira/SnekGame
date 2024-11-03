package com.example.snekgame.helpers

class GameConstants {
    class Face_Direction {
        companion object {
            const val DOWN = 0
            const val UP = 1
            const val LEFT = 2
            const val RIGHT = 3
        }
    }
    class Sprite {
        companion object {
            const val DEFAULT_SIZE = 16
            // Use 8 when testing with the emulator, otherwise, use 6 for real phones
            const val SCALE_MULTIPLIER = 6
            const val SIZE = DEFAULT_SIZE * SCALE_MULTIPLIER
        }
    }

    class Animation {
        companion object {
            const val SPEED = 10
            const val AMOUNT = 4    // How many different sprites there are in each animation
        }
    }

    class Player {
        companion object {
            // default is 300
            const val BASE_SPEED = 600
        }
    }
}