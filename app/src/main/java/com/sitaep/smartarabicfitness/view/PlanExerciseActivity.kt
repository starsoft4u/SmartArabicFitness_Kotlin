package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT
import com.kennyc.view.MultiStateView.VIEW_STATE_EMPTY
import com.sitaep.smartarabicfitness.Constants
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.PlanExerciseAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Exercise
import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.PlanExercise
import kotlinx.android.synthetic.main.activity_muscle.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

class PlanExerciseActivity : BaseActivity() {
    lateinit var plan: Plan
    var planExercises = arrayListOf<PlanExercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_tableview)

        plan = Klaxon().parse<Plan>(intent.getStringExtra("plan")!!)!!

        title = plan.name

        tableView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider_recycler_view, null))
            })
            adapter = PlanExerciseAdapter(
                context = this@PlanExerciseActivity,
                plan = plan,
                items = planExercises,
                onEdit = {
                    navigate(
                        ExerciseActivity::class.java,
                        "exercise" to it.exercise!!,
                        "plan" to plan,
                        "planExercise" to it
                    )
                },
                onDelete = {
                    planExercises.remove(it)
                    it.delete()
                    tableView.adapter?.notifyDataSetChanged()
                }
            ) {
                navigate(ExerciseActivity::class.java, "exercise" to it.exercise!!)
            }
        }

        loadPlanExercises()

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    private fun loadPlanExercises() {
        val items = LitePal.where("planId = ?", plan.identifier.toString()).find(PlanExercise::class.java)
        items.forEach {
            it.exercise = LitePal.where("identifier = ?", it.exerciseId.toString()).findFirst(Exercise::class.java)
        }
        planExercises.addAll(items)
        tableView.adapter?.notifyDataSetChanged()
        stateView.viewState = if (planExercises.isEmpty()) VIEW_STATE_EMPTY else VIEW_STATE_CONTENT
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (plan.isDefault) {
            return false
        }

        menu?.clear()
        menu?.add(0, Constants.MENU_ITEM_ADD, 0, "")
        menu?.get(0)?.apply {
            setIcon(R.drawable.ic_add)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == Constants.MENU_ITEM_ADD) {
            navigate(MuscleSelectActivity::class.java, "plan" to plan)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onAddPlanExercise(event: Events.AddPlanExercise) {
        Log.d(Constants.APP_KEY, "New events: $event")
        planExercises.add(event.planExercise)
        tableView.adapter?.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEditPlan(event: Events.EditPlanExercise) {
        Log.d(Constants.APP_KEY, "New events: $event")
        planExercises.firstOrNull { it.identifier == event.planExercise.identifier }?.apply {
            sets = event.planExercise.sets
            reps = event.planExercise.reps
            restTime = event.planExercise.restTime
            weight = event.planExercise.weight
        }
        tableView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

}
