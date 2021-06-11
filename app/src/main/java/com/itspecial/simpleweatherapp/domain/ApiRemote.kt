package com.itspecial.simpleweatherapp.domain

import com.itspecial.simpleweatherapp.Const.Addresses.Companion.API_HOST
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.TOKEN
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.UNITS
import com.itspecial.simpleweatherapp.data.ApiService
import com.itspecial.simpleweatherapp.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ApiRemote @Inject constructor() : Remote {

    private val lang = Locale.getDefault().language

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_HOST)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override suspend fun getWeather(lat: Double, lon: Double, exclude: String): WeatherResponse {
        Timber.d("MESSAGE $lat $lon $UNITS $lang $TOKEN")
        return service.weather(lat, lon, exclude, UNITS, lang, TOKEN)
    }
}