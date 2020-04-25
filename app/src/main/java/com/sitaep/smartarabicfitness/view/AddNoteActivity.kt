package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import androidx.core.view.get
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.Constants
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Log
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.layout_calendar.*
import org.litepal.LitePal

class AddNoteActivity : BaseActivity() {
    lateinit var plan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendar)

        setTitle(R.string.add_note)

        plan = Klaxon().parse<Plan>(intent.getStringExtra("plan")!!)!!

        tableView.visibility = GONE
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menu?.add(0, Constants.MENU_ITEM_SAVE, 0, R.string.save)
        menu?.get(0)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == Constants.MENU_ITEM_SAVE) {
            val log = Log(calendarView.firstSelectedDate.time, plan.identifier)
            LitePal.deleteAll(
                Log::class.java,
                "issuedat = ? AND planid = ?",
                log.issuedAt.time.toString(),
                log.planId.toString()
            )
            log.save()
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
