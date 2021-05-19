package com.itspecial.simpleweatherapp.di

import android.content.Context
import com.itspecial.simpleweatherapp.App
import com.itspecial.simpleweatherapp.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, RemoteModule::class, ForecastFragmentModule::class]
)

interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
