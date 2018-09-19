package ru.daryasoft.fintracker.periodicity

import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


private const val WORK_PERIODICITY_TAG = "rateWorker"

class PeriodicityWorkManager {

    fun onStart() {


        val statusesByTag = WorkManager.getInstance()?.getStatusesByTag(WORK_PERIODICITY_TAG)?.value
        if (statusesByTag == null || statusesByTag.isEmpty() || statusesByTag[0] == null || statusesByTag[0].state.isFinished) {
            Log.d("WorkerManager", "StartNewPeriodicityWorker")
            val periodicWorkRequest = PeriodicWorkRequest.Builder(PeriodicityWorker::class.java, 24, TimeUnit.HOURS, 1, TimeUnit.HOURS)
                    .addTag(WORK_PERIODICITY_TAG)
                    .build()
            WorkManager.getInstance()?.enqueue(periodicWorkRequest)
        }
    }

}