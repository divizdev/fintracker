package ru.daryasoft.fintracker.category.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.common.AppDatabase
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType
import javax.inject.Inject

/**
 * Репозиторий для работы с категориями.
 */
class CategoryRepositoryImpl @Inject constructor(db: AppDatabase) : CategoryRepository {
    override fun delete(category: Category) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCategory(category: Category) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val categories = MutableLiveData<List<Category>>()

    init {
        categories.value = mutableListOf(
                Category("Зарплата", TransactionType.INCOME
                ),
                Category("Сбережения", TransactionType.INCOME
                ),
                Category("Транспорт", TransactionType.OUTCOME
                ),
                Category("Еда", TransactionType.OUTCOME))
        launch {
            db.categoryDao().insert(Category("Зарплата", TransactionType.INCOME))
            db.categoryDao().insert(Category("Сбережения", TransactionType.INCOME))
            db.categoryDao().insert(Category("Транспорт", TransactionType.OUTCOME))
            db.categoryDao().insert(Category("Еда", TransactionType.OUTCOME))
        }
    }

    override fun getAll(): LiveData<List<Category>> {
        return categories
    }


    override fun findByTransactionType(transactionType: TransactionType): LiveData<List<Category>> {
        val categoriesByType = MutableLiveData<List<Category>>()
        categoriesByType.value = getAll().value?.filter { it.transactionType == transactionType }
        return categoriesByType
    }
}