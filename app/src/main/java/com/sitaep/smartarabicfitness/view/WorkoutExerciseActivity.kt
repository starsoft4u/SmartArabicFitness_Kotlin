package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import com.kennyc.view.MultiStateView
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.ExerciseAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Exercise
import com.sitaep.smartarabicfitness.model.Workout
import com.sitaep.smartarabicfitness.model.WorkoutExercise
import kotlinx.android.synthetic.main.layout_tableview.*
import org.litepal.LitePal

class WorkoutExerciseActivity : BaseActivity() {

    lateinit var workout: Workout
    var exercises = arrayListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_tableview)

        workout = Klaxon().parse<Workout>(intent.getStringExtra("workout")!!)!!
        val level = intent.getIntExtra("level", 1)
        setTitle(if (level == 1) R.string.beginner else R.string.advanced)

        // RecyclerView
        tableView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = ExerciseAdapter(context, exercises, true) {
                navigate(ExerciseActivity::class.java, "exercise" to it)
            }
        }

        floatButton.show()

        loadExercises(level)
    }

    private fun loadExercises(level: Int) {
        val workoutExercises = LitePal
            .where("workoutid = ? AND level = ?", workout.identifier.toString(), level.toString())
            .find(WorkoutExercise::class.java)
        val items = workoutExercises.map {
            LitePal
                .where("identifier = ?", it.exerciseId.toString())
                .findFirst(Exercise::class.java)
        }
        exercises.clear()
        exercises.addAll(items)
        tableView.adapter?.notifyDataSetChanged()
        stateView.viewState = if (exercises.isEmpty()) {
            MultiStateView.VIEW_STATE_EMPTY
        } else {
            MultiStateView.VIEW_STATE_CONTENT
        }
    }
}
