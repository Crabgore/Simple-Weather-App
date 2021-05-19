package com.itspecial.simpleweatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itspecial.simpleweatherapp.R
import com.itspecial.simpleweatherapp.common.getForecastDateString
import com.itspecial.simpleweatherapp.data.Daily
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class ForecastAdapter(
    private val context: Context,
    private val list: List<Daily>,
    private val mCityName: String,
    private val isNightMode: Boolean
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_adapter_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.setContent(list[position])

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val background: ImageView = itemView.findViewById(R.id.background)
        private val cityName: TextView = itemView.findViewById(R.id.city_name)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val temp: TextView = itemView.findViewById(R.id.temp)
        private val weatherImage: ImageView = itemView.findViewById(R.id.weather_image)
        private val weatherType: TextView = itemView.findViewById(R.id.weather_type)

        fun setContent(day: Daily) {
            val picasso = Picasso.get()
            val back = if (isNightMode) R.drawable.bg_night else R.drawable.bg_day
            picasso.load(back).error(back).fit().centerCrop().into(background)
            day.weather?.let {
                picasso.load("http://openweathermap.org/img/wn/${it[0].icon}@2x.png")
                    .into(weatherImage)
                weatherType.text = it[0].description
            }
            day.dt?.let {
                date.text = getForecastDateString(it)
            }
            cityName.text = mCityName
            temp.text =
                context.getString(R.string.temp_degrees, day.temp?.day?.roundToInt().toString())
        }
    }
}