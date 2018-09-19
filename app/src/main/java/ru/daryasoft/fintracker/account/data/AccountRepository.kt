package ru.daryasoft.fintracker.account.data

import android.arch.lifecycle.LiveData
import ru.daryasoft.fintracker.entity.Account

/**
 * Репозиторий для работы со счетами.
 */
interface AccountRepository {
    /**
     * Получить все счета.
     */
    fun getAll(): LiveData<List<Account>>

    /**
     * Удалить счет.
     */
    fun delete(account: Account)

    /**
     * Добавить счет.
     */
    fun add(account: Account)
}