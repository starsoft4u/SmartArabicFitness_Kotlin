package com.sitaep.smartarabicfitness.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kennyc.view.MultiStateView
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.WorkoutAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Workout
import kotlinx.android.synthetic.main.layout_tableview.*
import org.litepal.LitePal

class WorkoutActivity : BaseActivity() {

    private var workouts = arrayListOf<Workout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_tableview)

        setTitle(R.string.workouts)

        // RecyclerView
        tableView.apply {
            background = ColorDrawable(ContextCompat.getColor(context, R.color.lightGray))
            layoutManager = LinearLayoutManager(this@WorkoutActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = WorkoutAdapter(this@WorkoutActivity, workouts) {
                if (it.purchased) {
                    navigate(WorkoutDetailActivity::class.java, "workout" to it)
                }
            }
        }

        loadWorkouts()
    }

    private fun loadWorkouts() {
        val gender = if (IS_MALE) "female" else "male"
        val items = LitePal
            .where("gender <> ?", gender)
            .order("identifier ASC")
            .find(Workout::class.java)
        workouts.clear()
        workouts.addAll(items)
        tableView.adapter?.notifyDataSetChanged()
        stateView.viewState =
            if (workouts.isEmpty()) MultiStateView.VIEW_STATE_EMPTY else MultiStateView.VIEW_STATE_CONTENT
    }

}
