package com.itspecial.simpleweatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itspecial.simpleweatherapp.R
import com.itspecial.simpleweatherapp.common.getCurrentDateString
import com.itspecial.simpleweatherapp.data.Current
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class CurrentAdapter(
    private val context: Context,
    private val list: List<Current>
) : RecyclerView.Adapter<CurrentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.current_adapter_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.setContent(list[position])

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dt: TextView = itemView.findViewById(R.id.dt)
        private val image: ImageView = itemView.findViewById(R.id.weather_image)
        private val temp: TextView = itemView.findViewById(R.id.temp)

        fun setContent(day: Current) {
            day.weather?.let {
                Picasso.get().load("http://openweathermap.org/img/wn/${it[0].icon}@2x.png")
                    .into(image)
            }
            day.dt?.let {
                dt.text = getCurrentDateString(it)
            }
            temp.text =
                context.getString(R.string.temp_degrees, day.temp?.roundToInt().toString())
        }
    }

}