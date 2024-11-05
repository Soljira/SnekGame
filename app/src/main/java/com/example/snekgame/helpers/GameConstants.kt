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
            const val SCALE_MULTIPLIER = 8
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
            const val BASE_SPEED = 400
            const val BOUNDARY_X = 980
            const val BOUNDARY_Y = 1242
        }
    }

    // Ung golden frame around the map
    class Frame {
        companion object {
            // For Breiah
            const val WIDTH = 1440
            const val HEIGHT = 2100
        }
    }

    // Ung stone background sa controls UI
    class DpadBackground {
        companion object {
            // For Breiah
            const val WIDTH = 1080
            const val HEIGHT = 1335
        }
    }

    class Boundary {
        companion object {
            // Sets the limits of where the player and souls can go
            // Change this if it's different in your emulator
            // For Breiah Pixel 3 XL
            const val LEFT = 180
            const val TOP = 180
            const val RIGHT = 1933
            const val BOTTOM = 1940
        }
    }
}