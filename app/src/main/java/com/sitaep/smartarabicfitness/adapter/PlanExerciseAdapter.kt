package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.PlanExercise
import kotlinx.android.synthetic.main.layout_tableview_cell.view.*

class PlanExerciseAdapter(
    private val context: Context,
    private val plan: Plan,
    private val items: List<PlanExercise>,
    private val onEdit: (PlanExercise) -> Unit,
    private val onDelete: (PlanExercise) -> Unit,
    private val onItemClicked: (PlanExercise) -> Unit
) : RecyclerView.Adapter<PlanExerciseAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return if (plan.isDefault) items.size + 1 else items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val resId =
            if (viewType == 1) R.layout.layout_plan_summary else R.layout.layout_tableview_cell
        return ViewHolder(LayoutInflater.from(context).inflate(resId, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (plan.isDefault && position == 0) 1 else 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            return
        }

        val item = items[if (plan.isDefault) position - 1 else position]
        item.exercise?.let {
            val image = if (context.IS_MALE) it.maleImage else it.femaleImage
            try {
                val stream = context.assets.open(image)
                val bitmap = BitmapFactory.decodeStream(stream)
                holder.itemView.exerciseImage.setImageBitmap(bitmap)
                stream.close()
            } catch (_: Exception) {
                holder.itemView.exerciseImage.setImageResource(R.drawable.exercise)
            }
        }

        holder.itemView.nameView.text = item.exercise?.name(context)
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }

        if (plan.isDefault) {
            holder.itemView.repsText.visibility = GONE
            holder.itemView.restText.visibility = GONE
            holder.itemView.settingButton.visibility = GONE
        } else {
            holder.itemView.repsText.visibility = VISIBLE
            holder.itemView.restText.visibility = VISIBLE
            holder.itemView.settingButton.visibility = VISIBLE

            holder.itemView.repsText.text =
                context.getString(R.string.set_of_reps_, item.sets, item.reps)
            holder.itemView.restText.text =
                context.getString(R.string.rest_between_sets_, item.restTime)

            holder.itemView.settingButton.setOnClickListener {
                PopupMenu(context, it).apply {
                    inflate(R.menu.plan)
                    menu.removeItem(R.id.menuAddNote)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menuEdit -> onEdit.invoke(item)
                            R.id.menuDelete -> onDelete.invoke(item)
                            else -> return@setOnMenuItemClickListener false
                        }
                        return@setOnMenuItemClickListener true
                    }
                    show()
                }
            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}