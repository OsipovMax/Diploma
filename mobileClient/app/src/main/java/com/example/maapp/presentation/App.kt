package com.example.maapp.presentation

import android.app.Application
import com.example.maapp.BuildConfig
import com.example.maapp.presentation.di.AppComponent
import com.example.maapp.presentation.di.DaggerAppComponent
import timber.log.Timber

class App : Application(), AppComponent.ComponentProvider {

    override lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appComponent = DaggerAppComponent.builder()
                .context(this)
                .build()
    }

}