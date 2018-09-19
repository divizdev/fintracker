package ru.daryasoft.fintracker.category.data

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType
import javax.inject.Inject

class CategoryRepositoryDB @Inject constructor(val dao: CategoryDao) : CategoryRepository {
    override fun delete(category: Category) {
        launch {
            dao.delete(category)
        }
    }

    override fun addCategory(category: Category) {
        launch {
            dao.insert(category)
        }
    }

    override fun getAll(): LiveData<List<Category>> {
        return dao.getAll()
    }

    override fun findByTransactionType(transactionType: TransactionType): LiveData<List<Category>> {
        return dao.getByTransactionType(transactionType)
    }
}