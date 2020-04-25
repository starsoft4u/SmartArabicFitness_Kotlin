package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import com.sitaep.smartarabicfitness.R

class PlanFullActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_full)

        setTitle(R.string.full_body_plan)
    }
}
