package ru.daryasoft.fintracker.common

import android.content.Context
import android.os.Build
import ru.daryasoft.fintracker.entity.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Created by diviz on 10.02.2018.
 */

class LocaleUtils(context: Context?) {

    private val currentScale: Int
        get() = 2

    private val currentLocale:Locale? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context?.let{ context.resources.configuration.locales.get(0) }
    } else {
        context?.let { context.resources.configuration.locale }
    }

    fun formatBigDecimal(decimal: BigDecimal): String {
        val locale = currentLocale ?: Locale.getDefault()
        val decimalCopy = decimal.setScale(currentScale, RoundingMode.HALF_UP)
        val df = NumberFormat.getInstance(locale) as DecimalFormat
        df.minimumFractionDigits = currentScale
        df.isGroupingUsed = true
        return df.format(decimalCopy)
    }



    fun formatCurrency(currency: Currency): String {
        return when (currency) {
            Currency.RUB -> "\u20BD"
            Currency.USD -> "\u0024"
        }
    }

}


