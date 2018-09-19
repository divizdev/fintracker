package ru.daryasoft.fintracker.transaction.data

import android.arch.lifecycle.LiveData
import ru.daryasoft.fintracker.entity.Account
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionDB
import ru.daryasoft.fintracker.entity.TransactionUI
import java.math.BigDecimal

/**
 * Репозиторий для работы с финансовыми транзакциями.
 */
interface TransactionRepository {
    /**
     * Получить все финансовые транзакции.
     */
    fun getAll(): LiveData<List<TransactionUI>>

    /**
     * Ищет транзакции по категории и счету.
     *
     * @category категория, по которой нужно выполнить поиск.
     * @account счет, по которому нужно выполнить поиск.
     */
    fun query(category: Category, account: Account?): LiveData<List<TransactionUI>>

    /**
     * Добавляет транзакцию.
     *
     * @transactionDB транзакция для добавления.
     */
    fun add(transactionDB: TransactionDB, account: Account)

    /***
     * Удаляет транзакцию.
     *
     * @transactionDB транзакция для удаления.
     */
    fun delete(transactionDB: TransactionUI, value: BigDecimal)

    fun getPeriodicity(): List<TransactionDB>

    fun addPeriodicity(transactionDB: TransactionDB, idOldTransactionDB: Long, value: BigDecimal)

    fun getAccount(id: Long): Account
}