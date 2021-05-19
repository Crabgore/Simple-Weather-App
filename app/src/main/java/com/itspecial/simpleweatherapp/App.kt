package com.itspecial.simpleweatherapp

import com.itspecial.simpleweatherapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    /**
     * Сажаем Timber
     */
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    /**
     * стартуем даггер
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}