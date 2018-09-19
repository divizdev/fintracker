package ru.daryasoft.fintracker.category.data

import android.arch.lifecycle.LiveData
import ru.daryasoft.fintracker.common.AppDatabase
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType
import javax.inject.Inject

class CategoryRepositoryDB @Inject constructor(db: AppDatabase) : CategoryRepository  {

    private val dao = db.categoryDao()
    override fun getAll(): LiveData<List<Category>> {
        return dao.getAll()
    }

    override fun findByTransactionType(transactionType: TransactionType): LiveData<List<Category>> {
        return dao.getByTransactionType(transactionType)
    }
}