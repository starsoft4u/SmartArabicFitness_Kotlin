package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Workout
import kotlinx.android.synthetic.main.activity_workout_detail.*

class WorkoutDetailActivity : BaseActivity() {

    lateinit var workout: Workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        workout = Klaxon().parse<Workout>(intent.getStringExtra("workout")!!)!!

        title = workout.name(this)
        descriptionView.text = workout.description(this)
        goalView.text = workout.goal(this)?.replace('&', '\n')
        levelView.text = workout.level(this)
        equipmentView.text = workout.equipment(this)
        instructionView.text = workout.instruction(this)

        beginnerButton.setOnClickListener {
            navigate(WorkoutExerciseActivity::class.java, "workout" to workout, "level" to 1)
        }
        advancedButton.setOnClickListener {
            navigate(WorkoutExerciseActivity::class.java, "workout" to workout, "level" to 2)
        }
    }
}
