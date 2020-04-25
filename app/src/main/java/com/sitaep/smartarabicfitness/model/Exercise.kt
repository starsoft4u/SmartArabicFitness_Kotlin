package com.sitaep.smartarabicfitness.model

import android.content.Context
import com.beust.klaxon.JsonObject
import com.sitaep.smartarabicfitness.IS_EN
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

data class Exercise(
    @Column(unique = true)
    val identifier: Int,
    val muscleId: Int,
    val gender: String,
    val isPremium: Boolean,
    @Column(nullable = true)
    val enName: String?,
    @Column(nullable = true)
    val arName: String?,
    @Column(nullable = true)
    val enMainGroup: String?,
    @Column(nullable = true)
    val arMainGroup: String?,
    @Column(nullable = true)
    val enOtherGroup: String?,
    @Column(nullable = true)
    val arOtherGroup: String?,
    @Column(nullable = true)
    val enType: String?,
    @Column(nullable = true)
    val arType: String?,
    @Column(nullable = true)
    val enEquipment: String?,
    @Column(nullable = true)
    val arEquipment: String?,
    @Column(nullable = true)
    val enDifficulty: String?,
    @Column(nullable = true)
    val arDifficulty: String?,
    @Column(nullable = true)
    val enHowTo: String?,
    @Column(nullable = true)
    val arHowTo: String?
) : LitePalSupport(), Serializable {
    val maleImage: String
        get() = "gif/male/male$identifier.gif"

    val femaleImage: String
        get() = "gif/female/female$identifier.gif"

    companion object {
        fun fromJson(json: JsonObject): Exercise {
            val en = json.obj("en")!!
            val ar = json.obj("ar")!!

            return Exercise(
                identifier = json.int("id")!!,
                muscleId = json.int("muscleId") ?: 0,
                gender = json.string("gender") ?: "all",
                isPremium = json.boolean("premium") ?: false,
                enName = en.string("name"),
                enMainGroup = en.string("mainGroup"),
                enOtherGroup = en.string("otherGroup"),
                enType = en.string("type"),
                enEquipment = en.string("equipment"),
                enDifficulty = en.string("difficulty"),
                enHowTo = en.string("howto"),
                arName = ar.string("name"),
                arMainGroup = ar.string("mainGroup"),
                arOtherGroup = ar.string("otherGroup"),
                arType = ar.string("type"),
                arEquipment = ar.string("equipment"),
                arDifficulty = ar.string("difficulty"),
                arHowTo = ar.string("howto")
            )
        }
    }

    fun name(context: Context): String? = if (context.IS_EN) enName else arName
    fun mainGroup(context: Context): String? = if (context.IS_EN) enMainGroup else arMainGroup
    fun otherGroup(context: Context): String? = if (context.IS_EN) enOtherGroup else arOtherGroup
    fun type(context: Context): String? = if (context.IS_EN) enType else arType
    fun equipment(context: Context): String? = if (context.IS_EN) enEquipment else arEquipment
    fun difficulty(context: Context): String? = if (context.IS_EN) enDifficulty else arDifficulty
    fun howTo(context: Context): String? = if (context.IS_EN) enHowTo else arHowTo
}