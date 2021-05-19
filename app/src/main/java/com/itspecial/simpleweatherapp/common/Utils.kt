package com.itspecial.simpleweatherapp.common

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

/**
 * приводим ошибку ответа сервера к читаемому формату
 */
fun parseError(throwable: Throwable?): String? {
    return (throwable as HttpException).response()?.errorBody()?.string()?.replace("{", "")
        ?.replace("}", "")?.replace("\"", "")?.replace(":", ": ")
}

fun getForecastDateString(date: Long): String {
    val mDate = date * 1000
    val format = "EEEE, dd MMMM, yyyy"
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(mDate)
}

fun getCurrentDateString(date: Long): String {
    val mDate = date * 1000
    val format = "HH:mm"
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(mDate)
}

fun scaleView(v: View, startScale: Float, endScale: Float) {
    val anim: Animation = ScaleAnimation(
        startScale, endScale,  // Start and end values for the X axis scaling
        startScale, endScale,  // Start and end values for the Y axis scaling
        Animation.RELATIVE_TO_SELF, .5f,  // Pivot point of X scaling
        Animation.RELATIVE_TO_SELF, 0f // Pivot point of Y scaling
    )
    anim.fillAfter = true // Needed to keep the result of the animation
    anim.duration = 300
    v.startAnimation(anim)
}