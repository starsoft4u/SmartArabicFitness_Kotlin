package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Workout
import kotlinx.android.synthetic.main.layout_workout_cell.view.*

class WorkoutAdapter(
    private val context: Context,
    private val items: List<Workout>,
    private val onItemClicked: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private val images = arrayOf(
        R.drawable.workout_1,
        R.drawable.workout_2,
        R.drawable.workout_3,
        R.drawable.workout_4,
        R.drawable.workout_5,
        R.drawable.workout_6,
        R.drawable.workout_7,
        R.drawable.workout_8,
        R.drawable.workout_9,
        R.drawable.workout_10,
        R.drawable.workout_11,
        R.drawable.workout_12
    )

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_workout_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.workoutImage.setImageResource(images[item.identifier - 1])
        holder.itemView.nameView.text = item.name(context)
        holder.itemView.goalView.text = item.goal(context)
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }
        holder.itemView.lockImage.visibility = if (item.purchased) GONE else VISIBLE
        holder.itemView.overlayView.visibility = if (item.purchased) GONE else VISIBLE
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}