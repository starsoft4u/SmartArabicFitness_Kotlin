package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.adapter.ScheduleAdapter
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.activity_plan_schedule.*

class PlanScheduleActivity : BaseActivity() {

    lateinit var plan: Plan
    lateinit var dataSource: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_schedule)

        plan = Klaxon().parse<Plan>(intent.getStringExtra("plan")!!)!!
        dataSource = intent.getIntArrayExtra("dataSource")!!

        title = plan.name

        tableView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider_8dp, null))
            })
            adapter = ScheduleAdapter(context, dataSource)
        }
    }
}
