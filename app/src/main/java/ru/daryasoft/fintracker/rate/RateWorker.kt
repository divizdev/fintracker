package ru.daryasoft.fintracker.rate

import android.util.Log
import androidx.work.Worker
import ru.daryasoft.fintracker.initialization.FinTrackerApplication
import javax.inject.Inject

/**
 * Задача для обновления курса валют из сети.
 */
class RateWorker : Worker() {

    @Inject
    lateinit var rateRepository: RateRepository

    @Inject
    lateinit var rateNetworkDataSource: RateNetworkDataSource

    override fun doWork(): Result {
        if (applicationContext is FinTrackerApplication) {
            (applicationContext as FinTrackerApplication).mainComponent.injectRateWorker(this)
        }

        Log.d("RateWorker", "Start")

        val rates = rateNetworkDataSource.getRates().execute().body() ?: listOf()
        rateRepository.onRatesUpdate(rates)

        Log.d("RateWorker", "Finish")


        return Result.SUCCESS
    }
}