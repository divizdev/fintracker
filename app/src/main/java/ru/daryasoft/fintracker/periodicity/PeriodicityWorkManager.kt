package ru.daryasoft.fintracker.periodicity

import android.util.Log
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


private const val WORK_PERIODICITY_TAG = "rateWorker"

@Singleton
class PeriodicityWorkManager @Inject constructor(val transactionRepository: TransactionRepository) {

    fun onStart() {
        //Переносим периодические задания из фонового задания в процесс загрузки. Необходимо доработать реакцию на добавление периодического задания - сейчас происходит добавление транзакции в тихую
        launch {
            Log.d("PeriodicityWorker", "Start")

            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.add(Calendar.MONTH, -1)
            val date = cal.time
            cal.clear()

            val list = transactionRepository.getPeriodicity()
            for (transactionDB in list) {
                if (transactionDB.date < date) {
                    val value = transactionRepository.getAccount(transactionDB.idAccount
                            ?: -1).money.value.add(transactionDB.sum.value)
                    val newTransactionDB = transactionDB.copy()
                    newTransactionDB.id = null
                    newTransactionDB.date = Date()//Ошибка не правильная дата, нужно высчитывать месяц

                    transactionRepository.addPeriodicity(newTransactionDB, transactionDB.id
                            ?: -1, value ?: BigDecimal.valueOf(0))
                }
            }
            Log.d("PeriodicityWorker", "Finish")
        }
    }
}