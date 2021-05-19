package com.itspecial.simpleweatherapp.di.modules

import com.itspecial.simpleweatherapp.domain.ApiRemote
import com.itspecial.simpleweatherapp.domain.Remote
import dagger.Binds
import dagger.Module

@Module
abstract class RemoteModule {

    @Binds
    abstract fun provideRemote(download: ApiRemote): Remote
}