package ru.manzharovn.herofinder.presentation

import android.app.Application
import ru.manzharovn.herofinder.presentation.di.DaggerAppComponent

class MyApp : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}