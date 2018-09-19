package ru.daryasoft.fintracker.category.data

import android.arch.lifecycle.LiveData
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType

/**
 * Репозиторий для работы с категориями.
 */
interface CategoryRepository {
    fun getAll(): LiveData<List<Category>>

    fun findByTransactionType(transactionType: TransactionType): LiveData<List<Category>>
}