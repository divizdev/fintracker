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

    private val categories = MutableLiveData<List<Category>>()

    init {
        categories.value = mutableListOf(
                Category("Зарплата", TransactionType.INCOME,
                        "ic_salary"),
                Category("Сбережения", TransactionType.INCOME,
                        "ic_saving"),
                Category("Транспорт", TransactionType.OUTCOME,
                        "ic_transport"),
                Category("Еда", TransactionType.OUTCOME,
                        "ic_food"))
        launch {
            db.categoryDao().insert(Category("Зарплата", TransactionType.INCOME,"ic_salary"))
            db.categoryDao().insert(Category("Сбережения", TransactionType.INCOME,"ic_saving"))
            db.categoryDao().insert(Category("Транспорт", TransactionType.OUTCOME,"ic_transport"))
            db.categoryDao().insert(Category("Еда", TransactionType.OUTCOME,"ic_food"))
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