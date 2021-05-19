package com.itspecial.simpleweatherapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions =
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            requestMultiplePermissionLauncher.launch(permissions)
        }
    }

    private fun showPermissionError() {
        AlertDialog.Builder(this).apply {
            setMessage("Данному приложению необходимо разрешение на использование сервиса определения местоположения. Запросить ещё раз?")
            setCancelable(false)
            setPositiveButton("Да") { _, _ ->
                checkPermissions()
            }
            setNegativeButton("Выйти") { _, _ ->
                finish()
            }
            create().show()
        }
    }

    private val requestMultiplePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsMap ->
        resultsMap.forEach {
            Timber.d("Permission: ${it.key}, granted: ${it.value}")
            if (it.value != true) {
                showPermissionError()
                return@registerForActivityResult
            }
        }
        navController.navigate(R.id.forecastFragment)
    }
}