package com.itspecial.simpleweatherapp.ui.forecast

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.DAILY
import com.itspecial.simpleweatherapp.Const.MyPreferences.Companion.HOURLY
import com.itspecial.simpleweatherapp.base.BaseViewModel
import com.itspecial.simpleweatherapp.common.getCurrentDateString
import com.itspecial.simpleweatherapp.data.Current
import com.itspecial.simpleweatherapp.data.Daily
import com.itspecial.simpleweatherapp.data.WeatherResponse
import com.itspecial.simpleweatherapp.domain.Remote
import com.itspecial.simpleweatherapp.common.parseError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val hourlyLiveData: MutableLiveData<List<Current>> = MutableLiveData()
    val dailyLiveData: MutableLiveData<List<Daily>> = MutableLiveData()
    private var cityName = ""

    fun getDailyWeather(lat: Double, lon: Double) {
        Timber.d("Getting Weather Data $lat $lon")
        val disposable = remote.getWeather(lat, lon, HOURLY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onWeatherError)
            .subscribe(::parseWeatherResponse, ::handleFailure)

        addDisposable(disposable)
    }

    fun getHourlyWeather(lat: Double, lon: Double) {
        Timber.d("Getting Weather Data $lat $lon")
        val disposable = remote.getWeather(lat, lon, DAILY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onWeatherError)
            .subscribe(::parseHourlyResponse, ::handleFailure)

        addDisposable(disposable)
    }

    private fun onWeatherError(throwable: Throwable?) {
        Timber.d("Ошибка получения погоды' ${parseError(throwable)}")
    }

    private fun parseWeatherResponse(response: WeatherResponse) {
        Timber.d("Погода получена $response")
        dailyLiveData.value = response.daily
    }

    private fun parseHourlyResponse(response: WeatherResponse) {
        Timber.d("Погода получена $response")
        val list: MutableList<Current> = mutableListOf()
        response.hourly?.let {
            for (i in it.indices) {
                if (getCurrentDateString(it[i].dt!!) != "01:00")
                    list.add(it[i])
                else
                    break
            }
        }
        hourlyLiveData.value = list
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