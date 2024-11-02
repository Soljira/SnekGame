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
            const val SCALE_MULTIPLIER = 6
            const val SIZE = DEFAULT_SIZE * SCALE_MULTIPLIER
        }
    }
}