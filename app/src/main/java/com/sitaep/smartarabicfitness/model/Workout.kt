package com.sitaep.smartarabicfitness.model

import android.content.Context
import com.beust.klaxon.JsonObject
import com.sitaep.smartarabicfitness.IS_EN
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class Workout(
    @Column(unique = true)
    val identifier: Int,
    val gender: String,
    val levelCount: Int,
    val level1: String?,
    val level2: String?,
    val price: Double,
    val purchased: Boolean,
    @Column(nullable = true)
    val enName: String?,
    @Column(nullable = true)
    val arName: String?,
    @Column(nullable = true)
    val enGoal: String?,
    @Column(nullable = true)
    val arGoal: String?,
    @Column(nullable = true)
    val enEquipment: String?,
    @Column(nullable = true)
    val arEquipment: String?,
    @Column(nullable = true)
    val enDescription: String?,
    @Column(nullable = true)
    val arDescription: String?,
    @Column(nullable = true)
    val enInstruction: String?,
    @Column(nullable = true)
    val arInstruction: String?,
    @Column(nullable = true)
    val enLevel: String?,
    @Column(nullable = true)
    val arLevel: String?
) : LitePalSupport() {
    companion object {
        fun fromJson(json: JsonObject): Workout {
            val en = json.obj("en")!!
            val ar = json.obj("ar")!!

            return Workout(
                identifier = json.int("id")!!,
                price = json.double("price") ?: 0.0,
                purchased = json.boolean("isPurchased") ?: false,
                gender = json.string("gender") ?: "all",
                levelCount = json.int("levelCount") ?: 0,
                level1 = json.string("level1"),
                level2 = json.string("level2"),
                enName = en.string("name"),
                enGoal = en.string("goal"),
                enLevel = en.string("level"),
                enEquipment = en.string("equipment"),
                enDescription = en.string("description"),
                enInstruction = en.string("instruction"),
                arName = ar.string("name"),
                arGoal = ar.string("goal"),
                arLevel = ar.string("level"),
                arEquipment = ar.string("equipment"),
                arDescription = ar.string("description"),
                arInstruction = ar.string("instruction")
            )
        }
    }

    fun name(context: Context): String? = if (context.IS_EN) enName else arName
    fun goal(context: Context): String? = if (context.IS_EN) enGoal else arGoal
    fun level(context: Context): String? = if (context.IS_EN) enLevel else arLevel
    fun equipment(context: Context): String? = if (context.IS_EN) enEquipment else arEquipment
    fun description(context: Context): String? = if (context.IS_EN) enDescription else arDescription
    fun instruction(context: Context): String? = if (context.IS_EN) enInstruction else arInstruction
}
