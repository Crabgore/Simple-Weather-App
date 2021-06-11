package com.itspecial.simpleweatherapp.ui.forecast

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.liveData
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.DAILY
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.HOURLY
import com.itspecial.simpleweatherapp.base.BaseViewModel
import com.itspecial.simpleweatherapp.data.Resource
import com.itspecial.simpleweatherapp.domain.Remote
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    private var cityName = ""


    fun getDailyWeather(lat: Double, lon: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = remote.getWeather(lat, lon, HOURLY)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getHourlyWeather(lat: Double, lon: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = remote.getWeather(lat, lon, DAILY)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getCityName(geo: Geocoder, location: Location?): String {
        var result = ""
        location?.let {
            val addresses = geo.getFromLocation(it.latitude, it.longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val address = addresses[0]
                result = address.locality
            }
        }
        cityName = result
        return result
    }
}