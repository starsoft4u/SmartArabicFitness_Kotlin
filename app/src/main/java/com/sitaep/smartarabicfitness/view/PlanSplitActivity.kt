package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.widget.Button
import androidx.core.view.children
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.SplitButton
import kotlinx.android.synthetic.main.activity_plan_split.*

class PlanSplitActivity : BaseActivity() {
    lateinit var plan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_split)

        plan = Klaxon().parse<Plan>(intent.getStringExtra("plan")!!)!!

        title = plan.name
        noteText.text = plan.note

        // Buttons
        getButtons(plan.identifier).forEach { button ->
            layoutInflater.inflate(
                if (button.singleLine) R.layout.split_button_1 else R.layout.split_button_2,
                container
            )
            val view = container.children.last()
            if (view is Button) {
                view.setText(button.title)
            } else {
                view.findViewById<Button>(R.id.button).setText(button.title)
            }
            view.setOnClickListener {
                navigate(
                    PlanScheduleActivity::class.java,
                    "plan" to plan,
                    "dataSource" to button.dataSource
                )
            }
        }
    }

    private fun getButtons(planId: Int): List<SplitButton> {
        return when (planId) {
            4 -> arrayListOf(
                SplitButton(
                    R.string.exercise_muscle_once_a_week, true, intArrayOf(
                        R.string.monday, R.string.upper_body,
                        R.string.tuesday, R.string.rest,
                        R.string.wednesday, R.string.rest,
                        R.string.thursday, R.string.lower_body,
                        R.string.friday, R.string.rest,
                        R.string.saturday, R.string.rest,
                        R.string.sunday, R.string.rest
                    )
                ),
                SplitButton(
                    R.string.exercise_muscle_twice_a_week, true, intArrayOf(
                        R.string.monday, R.string.upper_body,
                        R.string.tuesday, R.string.lower_body,
                        R.string.wednesday, R.string.rest,
                        R.string.thursday, R.string.upper_body,
                        R.string.friday, R.string.lower_body,
                        R.string.saturday, R.string.rest
                    )
                ),
                SplitButton(
                    R.string.exercise_muscle_3_times_a_week, false, intArrayOf(
                        R.string.monday, R.string.upper_body,
                        R.string.tuesday, R.string.lower_body,
                        R.string.wednesday, R.string.upper_body,
                        R.string.thursday, R.string.lower_body,
                        R.string.friday, R.string.upper_body,
                        R.string.saturday, R.string.lower_body,
                        R.string.sunday, R.string.rest
                    )
                )
            )

            5 -> arrayListOf(
                SplitButton(
                    R.string.exercise_muscle_once_a_week, true, intArrayOf(
                        R.string.monday, R.string.feet,
                        R.string.tuesday, R.string.rest,
                        R.string.wednesday, R.string.back,
                        R.string.thursday, R.string.rest,
                        R.string.friday, R.string.chest,
                        R.string.saturday, R.string.rest,
                        R.string.sunday, R.string.rest
                    )
                ),
                SplitButton(
                    R.string.exercise_muscle_twice_a_week, true, intArrayOf(
                        R.string.monday, R.string.feet,
                        R.string.tuesday, R.string.back,
                        R.string.wednesday, R.string.chest,
                        R.string.thursday, R.string.feet,
                        R.string.friday, R.string.back,
                        R.string.saturday, R.string.chest,
                        R.string.sunday, R.string.rest
                    )
                )
            )

            6 -> arrayListOf(
                SplitButton(
                    R.string.method1, true, intArrayOf(
                        R.string.monday, R.string.feet,
                        R.string.tuesday, R.string.tricepts_chest,
                        R.string.wednesday, R.string.rest,
                        R.string.thursday, R.string.biceps_back,
                        R.string.friday, R.string.shoulders_abs,
                        R.string.saturday, R.string.rest,
                        R.string.sunday, R.string.rest
                    )
                ),
                SplitButton(
                    R.string.method2, true, intArrayOf(
                        R.string.monday, R.string.chest,
                        R.string.tuesday, R.string.feet,
                        R.string.wednesday, R.string.back,
                        R.string.thursday, R.string.rest,
                        R.string.friday, R.string.arms,
                        R.string.saturday, R.string.shoulders,
                        R.string.sunday, R.string.rest
                    )
                )
            )

            7 -> arrayListOf(
                SplitButton(
                    R.string.day1, true, intArrayOf(
                        R.string.bench_press, R.string._5_sets_5_reps,
                        R.string.squat, R.string._5_sets_5_reps,
                        R.string.ruden, R.string._5_sets_5_reps,
                        R.string.grunches, R.string._4_sets_15_20_reps,
                        R.string.dips, R.string._3_sets_15_reps
                    )
                ),
                SplitButton(
                    R.string.day2, true, intArrayOf(
                        R.string.squat, R.string._5_sets_5_reps,
                        R.string.pull_up, R.string._5_sets_5_reps,
                        R.string.deadlift, R.string._5_sets_5_reps,
                        R.string.bicepscurls, R.string._3_sets_10_12_reps,
                        R.string.shoulder_press, R.string._5_sets_5_reps
                    )
                ),
                SplitButton(
                    R.string.day3, true, intArrayOf(
                        R.string.squat, R.string._5_sets_5_reps,
                        R.string.bench_press, R.string._5_sets_5_reps,
                        R.string.crunches, R.string._5_sets_20_reps,
                        R.string.dumbbell_curls, R.string._3_sets_15_12_reps
                    )
                )
            )

            else -> arrayListOf()
        }
    }
}