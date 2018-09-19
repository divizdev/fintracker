package ru.daryasoft.fintracker.initialization

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.daryasoft.fintracker.periodicity.PeriodicityWorker
import ru.daryasoft.fintracker.rate.RateWorker
import javax.inject.Singleton

/**
 * Dagger-компонент.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    MainBindModule::class,
    MainProvideModule::class])
interface MainComponent : AndroidInjector<FinTrackerApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(app: Application): Builder

        fun build(): MainComponent
    }

    fun injectRateWorker(rateWorker: RateWorker)
    fun injectPeriodicityWorker(periodicityWorker: PeriodicityWorker)
}