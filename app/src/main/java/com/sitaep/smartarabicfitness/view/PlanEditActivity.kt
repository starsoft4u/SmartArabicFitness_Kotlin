package com.sitaep.smartarabicfitness.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.get
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.Constants
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.dismissKeyboard
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.activity_new_plan.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

class PlanEditActivity : BaseActivity() {

    var plan: Plan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_plan)

        setTitle(R.string.create_new_plan)

        intent?.getStringExtra("plan")?.let {
            plan = Klaxon().parse<Plan>(it)
        }

        nameEdit.setText(plan?.name)
        noteEdit.setText(plan?.note)

        expandPanel(nameToggleButton, false)
        expandPanel(noteToggleButton, false)

        nameToggleButton.setOnClickListener { togglePanel(it, !nameRadio.isChecked) }
        noteToggleButton.setOnClickListener { togglePanel(it, !noteRadio.isChecked) }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menu?.add(0, Constants.MENU_ITEM_SAVE, 0, R.string.save)
        menu?.get(0)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == Constants.MENU_ITEM_SAVE) {
            if (TextUtils.isEmpty(nameEdit.text) || TextUtils.isEmpty(noteEdit.text)) {
                return super.onOptionsItemSelected(item)
            }

            if (plan == null) {
                val maxId = LitePal.max(Plan::class.java, "identifier", Int::class.java) + 1
                val plan = Plan(maxId, nameEdit.text.toString(), noteEdit.text.toString(), false).apply {
                    save()
                }
                EventBus.getDefault().post(Events.AddPlan(plan))
            } else {
                plan?.apply {
                    name = nameEdit.text.toString()
                    note = noteEdit.text.toString()
                    save()
                }
                EventBus.getDefault().post(Events.EditPlan(plan!!))
            }
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun togglePanel(item: View, show: Boolean) {
        expandPanel(item, show)
        if (show) {
            if (item == nameToggleButton) {
                expandPanel(noteToggleButton, false)
            } else {
                expandPanel(nameToggleButton, false)
            }
        }
    }

    private fun expandPanel(item: View, show: Boolean) {
        item.dismissKeyboard()

        val visible = if (show) View.VISIBLE else View.GONE
        val parent = item.parent as ConstraintLayout

        parent.background = if (!show) {
            ColorDrawable(0xfff)
        } else if (item == nameToggleButton) {
            resources.getDrawable(R.drawable.shadow_bottom, null)
        } else {
            resources.getDrawable(R.drawable.shadow_top_bottom, null)
        }

        parent.children.filter { (it !is RadioButton) and (it != item) }.forEach {
            it.visibility = visible
        }

        (parent.children.find { it is RadioButton } as? RadioButton)?.isChecked = show
    }

}
