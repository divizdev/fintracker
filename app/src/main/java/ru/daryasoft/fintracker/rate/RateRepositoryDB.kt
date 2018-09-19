package ru.daryasoft.fintracker.rate

import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.Rate
import javax.inject.Inject

class RateRepositoryDB @Inject constructor(private val dao: RateDao): RateRepository {

    var listRate: List<Rate> = listOf()

    override fun getRateToDefault(currency: Currency): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRateFromDefault(currency: Currency): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRatesUpdate(newRates: List<Rate>) {
        listRate = newRates
        launch {
            dao.insertRates(newRates)
        }
    }
}