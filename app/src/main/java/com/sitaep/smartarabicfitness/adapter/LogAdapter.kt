package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Plan

class LogAdapter(
    private val context: Context,
    private val items: List<Plan>,
    private val onItemClicked: (Plan) -> Unit
) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_log_cell, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (holder.itemView is TextView) {
            holder.itemView.text = item.name
        }
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}