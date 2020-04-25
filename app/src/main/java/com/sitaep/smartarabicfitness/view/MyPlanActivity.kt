package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT
import com.kennyc.view.MultiStateView.VIEW_STATE_EMPTY
import com.sitaep.smartarabicfitness.Constants.Companion.APP_KEY
import com.sitaep.smartarabicfitness.Constants.Companion.MENU_ITEM_ADD
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.PlanAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Log
import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.PlanExercise
import kotlinx.android.synthetic.main.layout_tableview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

class MyPlanActivity : BaseActivity() {
    var plans = arrayListOf<Plan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_tableview)

        setTitle(R.string.my_plan)

        // RecyclerView
        tableView.apply {
            layoutManager = LinearLayoutManager(this@MyPlanActivity)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(this@MyPlanActivity, DividerItemDecoration.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider_recycler_view, null))
            })
            adapter = PlanAdapter(
                context = this@MyPlanActivity,
                items = plans,
                onAddNote = {
                    navigate(AddNoteActivity::class.java, "plan" to it)
                },
                onEdit = {
                    navigate(PlanEditActivity::class.java, "plan" to it)
                },
                onDelete = {
                    plans.remove(it)
                    LitePal.deleteAll(PlanExercise::class.java, "planid = ?", it.identifier.toString())
                    LitePal.deleteAll(
                        Log::class.java,
                        "planid = ?",
                        it.identifier.toString()
                    )
                    it.delete()
                    tableView.adapter?.notifyDataSetChanged()
                }
            ) {
                when (it.identifier) {
                    3 -> navigate(PlanFullActivity::class.java)
                    in 4..7 -> navigate(PlanSplitActivity::class.java, "plan" to it)
                    else -> navigate(PlanExerciseActivity::class.java, "plan" to it)
                }
            }
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        loadPlans()
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    private fun loadPlans() {
        val skip = if (IS_MALE) 2 else 1
        val data = LitePal
            .where("identifier <> ?", skip.toString())
            .order("identifier")
            .find(Plan::class.java)
            .map {
                when (it.identifier) {
                    1 -> it.name = getString(R.string.full_plan_man)
                    2 -> it.name = getString(R.string.full_plan_woman)
                    3 -> it.name = getString(R.string.full_body_plan)
                    4 -> {
                        it.name = getString(R.string.plan2_title)
                        it.note = getString(R.string.plan2_notes)
                    }
                    5 -> {
                        it.name = getString(R.string.plan3_title)
                        it.note = getString(R.string.plan3_notes)
                    }
                    6 -> {
                        it.name = getString(R.string.plan4_title)
                        it.note = getString(R.string.plan4_notes)
                    }
                    7 -> {
                        it.name = getString(R.string.plan5_title)
                        it.note = getString(R.string.plan5_notes)
                    }
                }
                return@map it
            }
        plans.addAll(data)
        tableView.adapter?.notifyDataSetChanged()
        stateView.viewState = if (plans.isEmpty()) VIEW_STATE_EMPTY else VIEW_STATE_CONTENT
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onAddPlan(event: Events.AddPlan) {
        android.util.Log.d(APP_KEY, "New events: $event")
        plans.add(event.plan)
        tableView.adapter?.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEditPlan(event: Events.EditPlan) {
        android.util.Log.d(APP_KEY, "New events: $event")
        plans.firstOrNull { it.identifier == event.plan.identifier }?.apply {
            name = event.plan.name
            note = event.plan.note
        }
        tableView.adapter?.notifyDataSetChanged()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menu?.add(0, MENU_ITEM_ADD, 0, "")
        menu?.get(0)?.apply {
            setIcon(R.drawable.ic_add)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == MENU_ITEM_ADD) {
            navigate(PlanEditActivity::class.java)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
