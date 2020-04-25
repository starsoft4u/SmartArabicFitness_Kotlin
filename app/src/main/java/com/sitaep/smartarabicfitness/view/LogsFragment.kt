package com.sitaep.smartarabicfitness.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class LogsFragment : Fragment() {
    var logs = arrayListOf<Log>()
    var plans = arrayListOf<Plan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    companion object {
        @JvmStatic
        fun newInstance() = LogsFragment()
    }
}
