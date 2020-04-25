package com.sitaep.smartarabicfitness

import android.content.Context
import android.content.SharedPreferences
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.Constants.Companion.APP_KEY

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
    if (!this.contains(key)) {
        return defaultValue
    }

    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
        Float::class -> getFloat(key, defaultValue as Float) as T
        Int::class -> getInt(key, defaultValue as Int) as T
        Long::class -> getLong(key, defaultValue as Long) as T
        String::class -> getString(key, defaultValue as String) as T
        List::class -> Klaxon().parseArray<String>(
            getString(key, null) ?: return defaultValue
        ).orEmpty().toList() as T
        ArrayList::class -> {
            mutableListOf<String>().apply {
                addAll(
                    Klaxon().parseArray<String>(
                        getString(key, null) ?: return defaultValue
                    ).orEmpty().toList()
                )
            } as T
        }
        else -> Klaxon().parse<T>(getString(key, null) ?: return defaultValue) ?: defaultValue
    }
}

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()

    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is String -> editor.putString(key, value)
        is ArrayList<*> -> {
            val list: ArrayList<*> = value
            val copy = arrayListOf<Any>().apply { addAll(list) }
            val json = Klaxon().toJsonString(copy)
            editor.putString(key, json)
        }
        else -> {
            val json = Klaxon().toJsonString(value)
            editor.putString(key, json)
        }
    }

    editor.apply()
}

var Context.IS_MALE: Boolean
    get() = applicationContext.getSharedPreferences(APP_KEY, 0).get("GENDER", true)!!
    set(value) = applicationContext.getSharedPreferences(APP_KEY, 0).put("GENDER", value)

var Context.AGE: Int
    get() = applicationContext.getSharedPreferences(APP_KEY, 0).get("AGE", 0)!!
    set(value) = applicationContext.getSharedPreferences(APP_KEY, 0).put("AGE", value)

var Context.WEIGHT: Float
    get() = applicationContext.getSharedPreferences(APP_KEY, 0).get("WEIGHT", 0f)!!
    set(value) = applicationContext.getSharedPreferences(APP_KEY, 0).put("WEIGHT", value)

var Context.LENGTH: Float
    get() = applicationContext.getSharedPreferences(APP_KEY, 0).get("LENGTH", 0f)!!
    set(value) = applicationContext.getSharedPreferences(APP_KEY, 0).put("LENGTH", value)

var Context.LANGUAGE: String
    get() = applicationContext.getSharedPreferences(APP_KEY, 0).get("LANGUAGE", "en")!!
    set(value) = applicationContext.getSharedPreferences(APP_KEY, 0).put("LANGUAGE", value)

val Context.IS_EN: Boolean
    get() = LANGUAGE == "en"