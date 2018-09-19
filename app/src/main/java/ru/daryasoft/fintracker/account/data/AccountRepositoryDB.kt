package ru.daryasoft.fintracker.account.data

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.common.AppDatabase
import ru.daryasoft.fintracker.entity.Account
import javax.inject.Inject

class AccountRepositoryDB @Inject constructor(db: AppDatabase) : AccountRepository {

    private val dao = db.accountDao()

    override fun getAll(): LiveData<List<Account>> {
        return dao.getAll()
    }

    override fun delete(account: Account) {
        launch { dao.delete(account) }
    }

    override fun add(account: Account) {
        launch {
            dao.insert(account)
        }
    }
}