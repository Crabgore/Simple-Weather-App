package com.itspecial.simpleweatherapp.di.modules

import androidx.lifecycle.ViewModel
import com.itspecial.simpleweatherapp.di.ViewModelBuilder
import com.itspecial.simpleweatherapp.di.ViewModelKey
import com.itspecial.simpleweatherapp.ui.forecast.ForecastViewModel
import com.itspecial.simpleweatherapp.ui.forecast.ForecastFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ForecastFragmentModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun forecastFragment(): ForecastFragment

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindViewModel(viewModel: ForecastViewModel): ViewModel
}