package com.itspecial.simpleweatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itspecial.simpleweatherapp.R

class DailyAdapter(
    private val context: Context,
    private val list: List<Double>
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.daily_adapter_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.setContent(list[position], position)

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val day: TextView = itemView.findViewById(R.id.day)
        private val temp: TextView = itemView.findViewById(R.id.temp)

        fun setContent(temperature: Double, position: Int) {
            when (position) {
                0 -> day.text = context.getString(R.string.morning)
                1 -> day.text = context.getString(R.string.day)
                2 -> day.text = context.getString(R.string.evening)
                3 -> day.text = context.getString(R.string.night)
            }
            temp.text = temperature.toString()
        }
    }
}