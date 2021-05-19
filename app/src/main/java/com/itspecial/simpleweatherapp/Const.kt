package com.itspecial.simpleweatherapp

object Const {
    class Addresses {
        companion object {
            const val API_HOST = "https://api.openweathermap.org/data/2.5/"
        }
    }

    class MyPreferences {
        companion object {
            const val TOKEN = "038a5a9df7a559719f7f6be0a27c70d2"
            const val HOURLY = "hourly"
            const val DAILY = "daily"
            const val UNITS = "metric"
        }
    }
}