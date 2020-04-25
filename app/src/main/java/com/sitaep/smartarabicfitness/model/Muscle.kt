package com.sitaep.smartarabicfitness.model

import android.content.Context
import com.beust.klaxon.JsonObject
import com.sitaep.smartarabicfitness.IS_EN
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class Muscle(
    @Column(unique = true)
    val identifier: Int,
    val enName: String,
    val arName: String
) : LitePalSupport() {
    companion object {
        fun fromJson(json: JsonObject): Muscle {
            return Muscle(
                identifier = json.int("id")!!,
                enName = json.string("en")!!,
                arName = json.string("ar")!!
            )
        }
    }

    fun name(context: Context): String = if (context.IS_EN) enName else arName
}