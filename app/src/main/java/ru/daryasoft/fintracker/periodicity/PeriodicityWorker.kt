package ru.daryasoft.fintracker.periodicity

import android.util.Log
import androidx.work.Worker
import ru.daryasoft.fintracker.initialization.FinTrackerApplication
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class PeriodicityWorker : Worker() {
    @Inject
    lateinit var transactionRepository: TransactionRepository


    override fun doWork(): Worker.Result {
        if (applicationContext is FinTrackerApplication) {
            (applicationContext as FinTrackerApplication).mainComponent.injectPeriodicityWorker(this)
        }

        Log.d("PeriodicityWorker", "Start")

        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.MONTH, 1)

        val date = cal.time

        cal.clear()


        val list = transactionRepository.getPeriodicity()
        for (transactionDB in list) {
            if (transactionDB.date < date) {
                val value = transactionRepository.getAccount(transactionDB.idAccount ?: -1).money.value.add(transactionDB.sum.value)
                val newTransactionDB = transactionDB.copy()
                newTransactionDB.id = null
                transactionRepository.addPeriodicity(transactionDB, newTransactionDB.id ?: -1, value ?: BigDecimal.valueOf(0))
            }
        }
        Log.d("PeriodicityWorker", "Finish")


        return Worker.Result.SUCCESS
    }
}