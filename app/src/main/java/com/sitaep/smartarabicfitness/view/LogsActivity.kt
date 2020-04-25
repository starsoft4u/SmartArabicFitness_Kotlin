package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.LogAdapter
import com.sitaep.smartarabicfitness.extensions.navigate
import com.sitaep.smartarabicfitness.model.Log
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.layout_calendar.*
import org.litepal.LitePal
import java.util.*

class LogsActivity : BaseActivity() {
    private var logs = arrayListOf<Log>()
    private var plans = arrayListOf<Plan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendar)

        setTitle(R.string.logs)

        // Load log data
        logs.addAll(LitePal.findAll(Log::class.java))

        // Calendar
        val events = logs.map {
            val calendar = Calendar.getInstance().apply { time = it.issuedAt }
            EventDay(calendar, R.drawable.ic_dot)
        }
        calendarView.setEvents(events)
        calendarView.setOnDayClickListener { day ->
            val selDate = day.calendar.time
            val notes = logs.filter { it.issuedAt == selDate }
                .map { LitePal.where("identifier = ?", it.planId.toString()).findFirst(Plan::class.java) }
            plans.clear()
            plans.addAll(notes)
            tableView.adapter?.notifyDataSetChanged()
        }

        // RecyclerView
        tableView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider_recycler_view, null))
            })
            adapter = LogAdapter(context, plans) {
                when (it.identifier) {
                    3 -> context?.navigate(PlanFullActivity::class.java)
                    in 4..7 -> context?.navigate(PlanSplitActivity::class.java, "plan" to it)
                    else -> context?.navigate(PlanExerciseActivity::class.java, "plan" to it)
                }
            }
        }
    }
}
