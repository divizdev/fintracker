package ru.daryasoft.fintracker.transaction.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ru.daryasoft.fintracker.entity.Account
import ru.daryasoft.fintracker.entity.TransactionDB
import ru.daryasoft.fintracker.entity.TransactionType
import ru.daryasoft.fintracker.entity.TransactionUI
import java.math.BigDecimal

//t.value, t.currency, t.date, c.idKeyCategory, c.name, c.transactionType, a.name as nameAccount, a.value as sumAccount,  t.id, t.idAccount , t.idCategory
@Dao
abstract class TransactionDao {
    @Query("SELECT t.value, t.currency, t.date, c.idKeyCategory, c.name, c.transactionType, a.name as nameAccount, a.value as sumAccount,  t.id, t.idAccount , t.idCategory, t.periodicity FROM TransactionDB as t JOIN account as a ON idAccount = a.id JOIN category as c on idCategory = c.idKeyCategory")
    abstract fun getAll(): LiveData<List<TransactionUI>>

    @Query("SELECT * FROM TransactionDB Where id = :id")
    abstract fun getById(id: Long): LiveData<TransactionDB>

    @Query("SELECT t.value, t.currency, t.date, c.idKeyCategory, c.name, c.transactionType, a.name as nameAccount, a.value as sumAccount,  t.id, t.idAccount , t.idCategory, t.periodicity FROM TransactionDB as t JOIN account as a ON idAccount = a.id JOIN category as c on idCategory = c.idKeyCategory where idCategory = :categoryId")
    abstract fun getQuery(categoryId: Long): LiveData<List<TransactionUI>>

    @Query("SELECT t.value, t.currency, t.date, c.idKeyCategory, c.name, c.transactionType, a.name as nameAccount, a.value as sumAccount,  t.id, t.idAccount , t.idCategory, t.periodicity FROM TransactionDB as t JOIN account as a ON idAccount = a.id JOIN category as c on idCategory = c.idKeyCategory where idCategory = :categoryId and idAccount = :accountId")
    abstract fun getQuery(categoryId: Long, accountId: Long): LiveData<List<TransactionUI>>

    @Query("SELECT Sum(TransactionDB.value) from TransactionDB join category on category.idKeyCategory = transactionDB.idCategory where category.transactionType = :transactionType and TransactionDB.idAccount = :accountId")
    abstract fun getSum(transactionType: TransactionType, accountId: Long): LiveData<BigDecimal>

    @Query("Select * From TransactionDB where isScheduled = 0 and periodicity = 1")
    abstract fun getPeriodicity(): List<TransactionDB>

    @Query("SELECT t.value, t.currency, t.date, c.idKeyCategory, c.name, c.transactionType, a.name as nameAccount, a.value as sumAccount,  t.id, t.idAccount , t.idCategory, t.periodicity FROM TransactionDB as t JOIN account as a ON idAccount = a.id JOIN category as c on idCategory = c.idKeyCategory where t.isScheduled = 0 and t.periodicity = 1")
    abstract fun getPeriodicityUI(): List<TransactionUI>

    @Query("Select * from account where id = :id")
    abstract fun getAccountById(id: Long): Account

    @Transaction
    open fun deleteTransaction(transactionUI: TransactionUI,  value: BigDecimal){
        updateAccount(transactionUI.idAccount ?: 0, value)
        delete(transactionUI.id ?: 0)
    }

    @Query("Delete from transactiondb where id = :id")
    abstract fun delete(id: Long)

    @Query("Update account set value = :value where id = :id")
    abstract fun updateAccount(id: Long, value: BigDecimal)

    @Query("UPDATE transactiondb set isScheduled = 1 where id = :id")
    abstract fun updateSchedule(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTransaction(transactionDB: TransactionDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAccount(account: Account)

    @Transaction
    open fun insertTransaction(transactionDB: TransactionDB, account: Account) {
        insertAccount(account)
        insertTransaction(transactionDB)//Имеет смысл заменить на update
    }

    @Transaction
    open fun insertPeriodicity(transactionDB: TransactionDB, idOldTransactionDB: Long, value: BigDecimal){
        insertTransaction(transactionDB)
        updateAccount(transactionDB.idAccount ?: -1, value)
        updateSchedule(idOldTransactionDB)
    }

}