package com.example.snekgame.ui

/**
 * Handles all the button assets and turns them into VectorDrawables
 */

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat  // added new dependencies in build.gradle.kts
import com.example.snekgame.R

class ButtonImages(context: Context) {
    var up: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_up, null)
    var down: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_down, null)
    var left: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_left, null)
    var right: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_right, null)

    var upPressed: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_up_pressed, null)
    var downPressed: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_down_pressed, null)
    var leftPressed: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_left_pressed, null)
    var rightPressed: Drawable? = VectorDrawableCompat.create(context.resources, R.drawable.button_right_pressed, null)
}
