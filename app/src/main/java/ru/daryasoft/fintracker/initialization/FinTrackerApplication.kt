package ru.daryasoft.fintracker.initialization

import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import dagger.android.support.HasSupportFragmentInjector
import ru.daryasoft.fintracker.periodicity.PeriodicityWorkManager
import ru.daryasoft.fintracker.rate.RateWorkerManager


/**
 * Класс приложения.
 */
class FinTrackerApplication : DaggerApplication(), HasActivityInjector, HasSupportFragmentInjector {
    val mainComponent = DaggerMainComponent
            .builder()
            .create(this)
            .build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return mainComponent
    }

    override fun onCreate() {
        super.onCreate()
        val rateWorkManager = RateWorkerManager()
        rateWorkManager.onStart()

        val periodicityWorkManager = PeriodicityWorkManager()
        periodicityWorkManager.onStart()
    }
}
