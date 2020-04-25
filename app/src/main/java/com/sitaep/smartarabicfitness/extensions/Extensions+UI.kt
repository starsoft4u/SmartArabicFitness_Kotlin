package com.sitaep.smartarabicfitness.extensions

import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon

// dp -> pixel (Float)
fun View.toPixel(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}

// pixel -> dp (Int)
fun View.toDP(pixel: Float): Float {
    return pixel * DisplayMetrics.DENSITY_DEFAULT / context.resources.displayMetrics.xdpi
}

enum class TouchRegion {
    ALL, TOP, DOWN, LEFT, RIGHT
}

fun View.contain(x: Float, y: Float, region: TouchRegion = TouchRegion.ALL): Boolean {
    if (visibility != VISIBLE) {
        return false
    }

    val pos = intArrayOf(0, 0)
    getLocationOnScreen(pos)
    val left = pos.component1().toFloat()
    val top = pos.component2().toFloat()

    return when (region) {
        TouchRegion.TOP -> (x in left..(left + width)) && (y in top..(top + height / 2))
        TouchRegion.DOWN -> (x in left..(left + width)) && (y in (top + height / 2)..(top + height))
        TouchRegion.LEFT -> (x in left..(left + width / 2)) && (y in top..(top + height))
        TouchRegion.RIGHT -> (x in (left + width / 2)..(left + width)) && (y in top..(top + height))
        else -> (x in left..(left + width)) && (y in top..(top + height))
    }
}

// Create view from context
fun <T : View> Context.create(clazz: Class<T>, init: T.() -> Unit): T {
    val constructor = clazz.getConstructor(Context::class.java)
    val view = constructor.newInstance(this)
    view.init()
    return view
}

// Create view and add to parent
fun <T : View> ViewGroup.create(clazz: Class<T>, init: T.() -> Unit): T {
    return context.create(clazz, init).also { addView(it) }
}
