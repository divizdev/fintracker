package ru.daryasoft.fintracker.rate

import android.arch.lifecycle.MutableLiveData
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.Rate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы с курсами валют.
 */
@Singleton
class RateRepositoryImpl @Inject constructor() : RateRepository {
    private val rates = MutableLiveData<Map<Currency, Rate>>()

    override fun onRatesUpdate(newRates: List<Rate>) {
        rates.postValue(newRates.map { it.currency to it }.toMap())
    }

    override fun getRateToDefault(currency: Currency): Double {
        return rates.value?.get(currency)?.ratio ?: if (currency == Currency.RUB) 1.0 else 66.29
    }

    override fun getRateFromDefault(currency: Currency): Double {
        return 1 / (rates.value?.get(currency)?.ratio
                ?: if (currency == Currency.RUB) 1.0 else 66.29)
    }

}