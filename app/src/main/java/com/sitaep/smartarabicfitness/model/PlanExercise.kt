package com.sitaep.smartarabicfitness.model

import com.beust.klaxon.JsonObject
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class PlanExercise(
    @Column(unique = true)
    val identifier: Int,
    val planId: Int,
    val exerciseId: Int,
    var sets: Int,
    var reps: Int,
    var restTime: Int,
    var weight: Int,
    @Column(ignore = true)
    var exercise: Exercise? = null
) : LitePalSupport() {
    companion object {
        fun fromJson(json: JsonObject): PlanExercise {
            return PlanExercise(
                identifier = json.int("id")!!,
                planId = json.int("planId") ?: 0,
                exerciseId = json.int("exerciseId") ?: 0,
                sets = json.int("sets") ?: 0,
                reps = json.int("reps") ?: 0,
                restTime = json.int("restTime") ?: 0,
                weight = json.int("weight") ?: 0
            )
        }
    }
}