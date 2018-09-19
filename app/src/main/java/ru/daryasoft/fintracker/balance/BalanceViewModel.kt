package ru.daryasoft.fintracker.balance

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.daryasoft.fintracker.account.data.AccountRepository
import ru.daryasoft.fintracker.common.Constants
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.rate.RateRepository
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

/**
 * ViewModel для баланса.
 */
class BalanceViewModel @Inject constructor(private val accountRepository: AccountRepository,
                                           private val rateRepository: RateRepository,
                                           private val transactionRepository: TransactionRepository) : ViewModel() {
    var _income: LiveData<BigDecimal> = MutableLiveData()

    val accounts by lazy {
        accountRepository.getAll()
    }

    val account by lazy {
        val liveData = MutableLiveData<Account>()
        liveData.value = accounts.value?.get(0)

        liveData
    }

    val currency by lazy {
        val liveData = MutableLiveData<Currency>()
        liveData.value = account.value?.money?.currency ?: Constants.DEFAULT_CURRENCY
        liveData
    }

    fun getIncome(account: Account): LiveData<BigDecimal>{
        return transactionRepository.sum(TransactionType.INCOME, account)
    }

    fun getOutcome(account: Account): LiveData<BigDecimal>{
        return transactionRepository.sum(TransactionType.OUTCOME, account)
    }

    val balance = MutableLiveData<Balance>()


    fun onChangeAccount(newAccount: Account) {
        account.value = newAccount
        onChangeCurrency(account.value?.money?.currency ?: Constants.DEFAULT_CURRENCY)

    }

    fun onChangeCurrency(newCurrency: Currency) {
        currency.value = newCurrency
        recalculateBalance()
    }

    fun recalcValue(bigDecimal: BigDecimal): BigDecimal{ //Этот нуждает в рефакторинге
        val currencyValue = currency.value ?: Constants.DEFAULT_CURRENCY
        val rateToDefault = rateRepository.getRateToDefault(account.value?.money?.currency
                ?: Constants.DEFAULT_CURRENCY)
        val rateFromDefault = rateRepository.getRateFromDefault(currencyValue)
        val newSum = (bigDecimal.toDouble()) * rateToDefault * rateFromDefault
        return newSum.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    }

    private fun recalculateBalance() {
        val currencyValue = currency.value ?: Constants.DEFAULT_CURRENCY
        val rateToDefault = rateRepository.getRateToDefault(account.value?.money?.currency
                ?: Constants.DEFAULT_CURRENCY)
        val rateFromDefault = rateRepository.getRateFromDefault(currencyValue)
        val newSum = (account.value?.money?.value?.toDouble()
                ?: 0.0) * rateToDefault * rateFromDefault
        balance.value = Balance(Money(BigDecimal.valueOf(newSum), currencyValue))

    }
}