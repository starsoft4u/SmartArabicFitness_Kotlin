package com.sitaep.smartarabicfitness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.model.Exercise
import kotlinx.android.synthetic.main.layout_tableview_cell.view.*
import pl.droidsonroids.gif.GifDrawable

class ExerciseAdapter(
    private val context: Context,
    private val items: List<Exercise>,
    private val hasHeader: Boolean = false,
    private val onItemClicked: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return if (hasHeader) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasHeader && position == 0) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == 1) R.layout.layout_workout_summary else R.layout.layout_tableview_cell
        return ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            return
        }

        val item = items[if (hasHeader) position - 1 else position]

        val drawable = if (context.IS_MALE)
            GifDrawable(context.assets, item.maleImage)
        else
            GifDrawable(context.assets, item.femaleImage)
        holder.itemView.exerciseImage.setImageDrawable(drawable.apply { stop() })

        holder.itemView.nameView.text = item.name(context)
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }
        holder.itemView.settingButton.visibility = View.GONE
        holder.itemView.repsText.visibility = View.GONE
        holder.itemView.restText.visibility = View.GONE
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}