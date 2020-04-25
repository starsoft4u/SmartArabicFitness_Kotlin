package com.sitaep.smartarabicfitness.model

import com.beust.klaxon.JsonObject
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class WorkoutExercise(
    @Column(unique = true)
    val identifier: Int,
    val workoutId: Int,
    val exerciseId: Int,
    val level: Int
) : LitePalSupport() {
    companion object {
        fun fromJson(json: JsonObject): WorkoutExercise {
            return WorkoutExercise(
                identifier = json.int("id")!!,
                workoutId = json.int("workoutId")!!,
                exerciseId = json.int("exerciseId")!!,
                level = json.int("level")!!
            )
        }
    }
}