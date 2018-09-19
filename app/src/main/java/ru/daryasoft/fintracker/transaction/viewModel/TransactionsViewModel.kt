package ru.daryasoft.fintracker.transaction.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.daryasoft.fintracker.common.SingleLiveEvent
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import javax.inject.Inject

/**
 * ViewModel для списка транзакций.
 */
class TransactionsViewModel @Inject constructor(private val transactionRepository: TransactionRepository) : ViewModel() {

    private val _showDialogDelete = SingleLiveEvent<Any>()
    private var positionDelete: Int = -1
    private var _positionCancelDelete: MutableLiveData<Int> = MutableLiveData()

    val positionCancelDelete: LiveData<Int>
        get() = _positionCancelDelete

    val showDialogDelete: LiveData<Any>
        get() = _showDialogDelete

    val _transactions: LiveData<List<TransactionUI>> by lazy {
        transactionRepository.getAll()
    }

    fun onDeleteTransaction(position: Int) {
        positionDelete = position
        _showDialogDelete.call()
    }

    fun onConfirmDeleteTransaction() {
        if (positionDelete != -1) {
            _transactions.value?.get(positionDelete)?.let {
                val sum = it.sumAccount.minus(it.sum.value)
                transactionRepository.delete(it, sum)
            }
            positionDelete = -1
        }
    }

    fun onCancelDeleteTransaction() {
        if (positionDelete != -1) {
            _positionCancelDelete.value = positionDelete
            positionDelete = -1
        }
    }


    fun onAddTransaction(account: Account, transactionDB: TransactionDB, category: Category) {
        transactionDB.isScheduled = transactionDB.periodicity == Periodicity.Without

        transactionDB.idCategory = category.idKeyCategory
        transactionDB.idAccount = account.id
        account.money.value = transactionDB.account.money.value.add(if (transactionDB.category.transactionType == TransactionType.INCOME)
            transactionDB.sum.value else -transactionDB.sum.value)

        transactionRepository.add(transactionDB, account)
    }
}