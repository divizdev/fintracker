package ru.daryasoft.fintracker.account.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ru.daryasoft.fintracker.entity.Account

@Dao
abstract class AccountDao {


    @Query("SELECT * FROM Account")
    abstract fun getAll(): LiveData<List<Account>>

    @Query("SELECT * FROM Account Where id = :id")
    abstract fun getById(id: Long): Account

    @Transaction
    open fun delete(account: Account){
        deleteAccount(account)
        account.id?.let{
            deleteTransaction(account.id ?: 0)
        }

    }

    @Query("DELETE from TransactionDB where idAccount = :idAccount")
    abstract fun deleteTransaction(idAccount: Long)//Почему-то не срабатывает внешний ключ, делаем каскадное удаление руками

    @Delete
    abstract fun deleteAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(account: Account)

}