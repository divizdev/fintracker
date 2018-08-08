package ru.daryasoft.fintracker.category.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.daryasoft.fintracker.category.data.CategoryRepository
import ru.daryasoft.fintracker.common.SingleLiveEvent
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType
import javax.inject.Inject

/**
 * ViewModel для списка категорий.
 */
class CategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {

    private val _showDialogDelete = SingleLiveEvent<Any>()
    private var positionDelete: Int = -1
    private var _positionCancelDelete: MutableLiveData<Int> = MutableLiveData()
    val showDialogDelete: LiveData<Any>
        get() = _showDialogDelete

    val positionCancelDelete: LiveData<Int>
        get() = _positionCancelDelete

    val categories: LiveData<List<Category>> by lazy {
        categoryRepository.getAll()
    }

    fun getCategoriesByType(transactionType: TransactionType): LiveData<List<Category>> {
        return categoryRepository.findByTransactionType(transactionType)
    }

    fun onDeleteCategory(position: Int) {
        positionDelete = position
        _showDialogDelete.call()
    }

    fun onConfirmDeleteCategory() {
        if (positionDelete != -1) {
            (categories.value?.let {
                categoryRepository.delete(it[positionDelete] )
                positionDelete = -1
            })
        }
    }

    fun onCancelDeleteCategory() {
        if (positionDelete != -1) {
            _positionCancelDelete.value = positionDelete
            positionDelete = -1
        }
    }

}