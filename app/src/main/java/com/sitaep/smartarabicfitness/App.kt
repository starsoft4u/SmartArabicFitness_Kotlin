package com.sitaep.smartarabicfitness

import android.app.Application
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.sitaep.smartarabicfitness.model.*
import org.litepal.LitePal

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        LitePal.initialize(this)
        LitePal.getDatabase()

        if (!LitePal.isExist(Muscle::class.java)) {
            // Load initial data
            val data = Parser.default().parse(resources.openRawResource(R.raw.data)) as JsonObject
            data.array<JsonObject>("muscles")
                ?.map { Muscle.fromJson(it) }
                ?.let { LitePal.saveAll(it) }

            data.array<JsonObject>("exercices")
                ?.map { Exercise.fromJson(it) }
                ?.let { LitePal.saveAll(it) }

            data.array<JsonObject>("plans")
                ?.map { Plan.fromJson(it) }
                ?.let { LitePal.saveAll(it) }

            data.array<JsonObject>("planExercices")
                ?.map { PlanExercise.fromJson(it) }
                ?.let { LitePal.saveAll(it) }

            data.array<JsonObject>("workouts")
                ?.map { Workout.fromJson(it) }
                ?.let { LitePal.saveAll(it) }

            data.array<JsonObject>("workoutsExercises")
                ?.map { WorkoutExercise.fromJson(it) }
                ?.let { LitePal.saveAll(it) }
        }
    }
}