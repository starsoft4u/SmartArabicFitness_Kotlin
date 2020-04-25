package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Plan
import kotlinx.android.synthetic.main.layout_tableview_cell.view.*

class PlanAdapter(
    private val context: Context,
    private val items: List<Plan>,
    private val onEdit: (Plan) -> Unit,
    private val onAddNote: (Plan) -> Unit,
    private val onDelete: (Plan) -> Unit,
    private val onItemClicked: (Plan) -> Unit
) : RecyclerView.Adapter<PlanAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_tableview_cell, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.nameView.text = item.name
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }
        holder.itemView.settingButton.setOnClickListener {
            PopupMenu(context, it).apply {
                inflate(R.menu.plan)
                if (item.isDefault) {
                    menu.removeItem(R.id.menuEdit)
                    menu.removeItem(R.id.menuDelete)
                }
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menuEdit -> onEdit.invoke(item)
                        R.id.menuAddNote -> onAddNote.invoke(item)
                        R.id.menuDelete -> onDelete.invoke(item)
                        else -> return@setOnMenuItemClickListener false
                    }
                    return@setOnMenuItemClickListener true
                }
                show()
            }
        }
        holder.itemView.repsText.visibility = GONE
        holder.itemView.restText.visibility = GONE
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}