package com.itspecial.simpleweatherapp.domain

import com.itspecial.simpleweatherapp.data.WeatherResponse
import io.reactivex.Single

interface Remote {
    fun getWeather(lat: Double, lon: Double, exclude: String): Single<WeatherResponse>
}