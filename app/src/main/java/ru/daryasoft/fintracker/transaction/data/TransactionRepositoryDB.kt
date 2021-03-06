package ru.daryasoft.fintracker.transaction.data

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.entity.*
import java.math.BigDecimal
import javax.inject.Inject

class TransactionRepositoryDB @Inject constructor(private val dao: TransactionDao) : TransactionRepository {
    override fun sum(transactionType: TransactionType, account: Account): LiveData<BigDecimal> {
       return dao.getSum(transactionType, account.id ?: -1)
    }

    override fun getTransactionById(id: Long): LiveData<TransactionDB> {
        return dao.getById(id)
    }

    override fun getAccount(id: Long): Account {
        return dao.getAccountById(id)
    }

    override fun addPeriodicity(transactionDB: TransactionDB, idOldTransactionDB: Long, value: BigDecimal) {
        launch {
            dao.insertPeriodicity(transactionDB, idOldTransactionDB, value)
        }
    }

    override fun getPeriodicity(): List<TransactionDB> {
        return dao.getPeriodicity()
    }


    private var transactions = dao.getAll()

    override fun getAll(): LiveData<List<TransactionUI>> {
        return transactions
    }

    override fun query(category: Category, account: Account?): LiveData<List<TransactionUI>> {
        return if (account == null) {
            dao.getQuery(category.idKeyCategory?: -1)
        } else {
            dao.getQuery(category.idKeyCategory?: -1, account.id ?: -1)
        }
    }

    override fun add(transactionDB: TransactionDB, account: Account) {
        launch {
            dao.insertTransaction(transactionDB, account)
        }
    }

    override fun delete(transactionUI: TransactionUI, value: BigDecimal) {
        launch {
            dao.deleteTransaction(transactionUI, value)
        }
    }
}