package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT
import com.kennyc.view.MultiStateView.VIEW_STATE_EMPTY
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.ExerciseAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Exercise
import com.sitaep.smartarabicfitness.model.Muscle
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.activity_muscle.*
import org.litepal.LitePal

class MuscleDetailActivity : BaseActivity() {

    var plan: Plan? = null
    var muscleId = 0
    var muscles = arrayListOf<Muscle>()
    var exercises = arrayListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muscle)

        muscleId = intent.getIntExtra("muscleId", 0)
        intent.getStringExtra("plan")?.let {
            plan = Klaxon().parse<Plan>(it)
        }

        // RecyclerView
        tableView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider_recycler_view, null))
            })
            adapter = ExerciseAdapter(context, exercises) {
                if (plan != null) {
                    navigate(ExerciseActivity::class.java, "exercise" to it, "plan" to plan!!)
                } else {
                    navigate(ExerciseActivity::class.java, "exercise" to it)
                }
            }
        }

        loadMuscles()
    }

    private fun loadMuscles() {
        muscles = arrayListOf(
            Muscle(
                0,
                getString(R.string.all_exercise),
                getString(R.string.all_exercise)
            )
        )
        muscles.addAll(LitePal.order("identifier").find(Muscle::class.java))
        val items = muscles.map { it.name(this) }
        muscleSelect.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, items).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        muscleSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                exercises.clear()
                tableView.adapter?.notifyDataSetChanged()
                setTitle(R.string.app_name)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                loadExercises(muscles[position].identifier)
                title = muscles[position].name(this@MuscleDetailActivity)
            }
        }

        val defaultMuscle = muscles.find { it.identifier == muscleId } ?: muscles.firstOrNull()
        defaultMuscle?.let {
            muscleSelect.setSelection(muscles.indexOf(it))
            loadExercises(it.identifier)
            title = it.name(this)
        }
    }

    private fun loadExercises(muscleId: Int) {
        exercises.clear()
        val gender = if (IS_MALE) "female" else "male"
        if (muscleId == 0) {
            exercises.addAll(
                LitePal
                    .where("isPremium = 0 AND gender <> ?", gender)
                    .find(Exercise::class.java)
            )
        } else {
            exercises.addAll(
                LitePal
                    .where(
                        "isPremium = 0 AND gender <> ? AND muscleId = ?",
                        gender,
                        muscleId.toString()
                    )
                    .find(Exercise::class.java)
            )
        }
        tableView.adapter?.notifyDataSetChanged()
        tableView.smoothScrollToPosition(0)
        stateView.viewState = if (exercises.isEmpty()) VIEW_STATE_EMPTY else VIEW_STATE_CONTENT
    }
}
