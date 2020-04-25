package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Plan

class MuscleSelectActivity : BaseActivity() {

    lateinit var plan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        plan = Klaxon().parse<Plan>(intent.getStringExtra("plan")!!)!!

        setTitle(R.string.select_exercise)
//
//        muscleList.children.filter { it is TextView }.forEach {
//            it.setOnClickListener { item ->
//                val muscleId = try {
//                    item.tag.toString().toInt()
//                } catch (_: Exception) {
//                    0
//                }
//                navigate(MuscleDetailActivity::class.java, "muscleId" to muscleId, "plan" to plan)
//            }
//        }

    }

}
