package com.sitaep.smartarabicfitness.extensions

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.R
import java.io.Serializable

// Navigate Activity
fun <T : AppCompatActivity> Context.intentForNavigate(
    activity: Class<T>,
    vararg args: Pair<String, Any>
): Intent {
    val intent = Intent(this, activity)
    args.forEach { param ->
        val value = param.second
        when (value) {
            is Int -> intent.putExtra(param.first, value)
            is String -> intent.putExtra(param.first, value)
            is Boolean -> intent.putExtra(param.first, value)
            is Long -> intent.putExtra(param.first, value)
            is Float -> intent.putExtra(param.first, value)
            is Double -> intent.putExtra(param.first, value)
            is IntArray -> intent.putExtra(param.first, value)
            is FloatArray -> intent.putExtra(param.first, value)
            is Array<*> -> intent.putExtra(param.first, value)
            is Parcelable -> intent.putExtra(param.first, value)
            is Serializable -> intent.putExtra(param.first, value)
            else -> intent.putExtra(param.first, Klaxon().toJsonString(value))
        }
    }
    return intent
}

fun <T : AppCompatActivity> Context.navigate(activity: Class<T>, vararg args: Pair<String, Any>) {
    val intent = intentForNavigate(activity, *args)
    startActivity(intent)
}

fun <T : AppCompatActivity> Context.navigateClear(activity: Class<T>, vararg args: Pair<String, Any>) {
    val intent = intentForNavigate(activity, *args).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun <T : AppCompatActivity> Context.navigateResult(
    activity: Class<T>,
    requestCode: Int,
    vararg args: Pair<String, Any>
) {
    if (this is AppCompatActivity) {
        val intent = intentForNavigate(activity, *args)
        startActivityForResult(intent, requestCode)
    }
}

// Audio
fun Context.playAudio(res: Int) {
    MediaPlayer.create(this, res)?.start()
}

// Hide keyboard
fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

// Device
val Context.isPhone: Boolean
    get() = resources.getBoolean(R.bool.isPhone)

val Context.isTablet: Boolean
    get() = resources.getBoolean(R.bool.isTablet)

val Context.isLandscape: Boolean
    get() = resources.getBoolean(R.bool.isLandscape)
