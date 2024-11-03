package com.example.snekgame.entities

import android.graphics.PointF
import android.graphics.RectF

/**
 * Superclass of all entities. General information like position, hitbox, and IDs will be stored here.
 */

abstract class Entity(
    initialPosition : PointF,
    width : Float,
    height : Float
) {
//
//    val hitbox: RectF = RectF(position.x, position.y, width, height)
//
//    // Returns the top-left corner of the hitbox as the entity's position
//    var position: PointF = PointF(0F, 0F)
//        get() = PointF(hitbox.left, hitbox.top)
    var position: PointF = initialPosition
        set(value) {
            field = value
            // Update hitbox when position changes
            hitbox.left = value.x
            hitbox.top = value.y
            hitbox.right = value.x + hitbox.width()
            hitbox.bottom = value.y + hitbox.height()
        }

        val hitbox: RectF = RectF(
            position.x,
            position.y,
            position.x + width,
            position.y + height
        )
    //    fun getHitbox(): RectF = hitbox
}