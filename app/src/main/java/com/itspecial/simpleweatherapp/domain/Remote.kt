package com.itspecial.simpleweatherapp.domain

import com.itspecial.simpleweatherapp.data.WeatherResponse

interface Remote {
    suspend fun getWeather(lat: Double, lon: Double, exclude: String): WeatherResponse
}