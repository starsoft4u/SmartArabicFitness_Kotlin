package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.R
import kotlinx.android.synthetic.main.layout_schedule.view.*

class ScheduleAdapter(
    private val context: Context,
    private val items: IntArray
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size / 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_schedule, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.leftText.setText(items[position * 2])
        holder.itemView.rightText.setText(items[position * 2 + 1])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}