package com.example.snekgame.helpers.interfaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.snekgame.helpers.GameConstants

interface BitmapMethods {
    companion object {
        val options = BitmapFactory.Options()
    }

    open fun getScaledBitmap(bitmap : Bitmap) : Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            bitmap.width * GameConstants.Sprite.SCALE_MULTIPLIER,
            bitmap.height * GameConstants.Sprite.SCALE_MULTIPLIER,
            false
        )
    }


}