package com.itspecial.simpleweatherapp.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class WeatherResponse (
    val lat: Double? = null,
    val lon: Double? = null,
    val timezone: String? = null,
    val timezoneOffset: Long? = null,
    val current: Current? = null,
    val minutely: List<Minutely>? = null,
    val daily: List<Daily>? = null,
    val hourly: List<Current>? = null
)

@JsonClass(generateAdapter = true)
data class Current (
    val dt: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double? = null,
    @field:Json(name="feels_like")
    val feelsLike: Double? = null,
    val pressure: Long? = null,
    val humidity: Long? = null,
    val dewPoint: Double? = null,
    val uvi: Double? = null,
    val clouds: Long? = null,
    val visibility: Long? = null,
    @field:Json(name="wind_speed")
    val windSpeed: Double? = null,
    val windDeg: Long? = null,
    val weather: List<Weather>? = null
)

@JsonClass(generateAdapter = true)
@Parcelize
data class Weather (
    val id: Long? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Daily (
    val dt: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val moonrise: Long? = null,
    val moonset: Long? = null,
    val moonPhase: Double? = null,
    val temp: Temp? = null,
    @field:Json(name="feels_like")
    val feelsLike: FeelsLike? = null,
    val pressure: Long? = null,
    val humidity: Long? = null,
    val dewPoint: Double? = null,
    @field:Json(name="wind_speed")
    val windSpeed: Double? = null,
    val windDeg: Long? = null,
    val windGust: Double? = null,
    val weather: List<Weather>? = null,
    val clouds: Long? = null,
    val pop: Double? = null,
    val rain: Double? = null,
    val uvi: Double? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class FeelsLike (
    val day: Double? = null,
    val night: Double? = null,
    val eve: Double? = null,
    val morn: Double? = null
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Temp (
    val day: Double? = null,
    val min: Double? = null,
    val max: Double? = null,
    val night: Double? = null,
    val eve: Double? = null,
    val morn: Double? = null
): Parcelable

@JsonClass(generateAdapter = true)
data class Minutely (
    val dt: Long? = null,
    val precipitation: Double? = null
)
