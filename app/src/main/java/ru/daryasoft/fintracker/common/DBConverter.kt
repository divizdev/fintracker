package ru.daryasoft.fintracker.common

import android.arch.persistence.room.TypeConverter
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.Periodicity
import ru.daryasoft.fintracker.entity.TransactionType
import java.math.BigDecimal
import java.util.*


class BDConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (date?.time)
    }

    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return if (value == null) null else BigDecimal(value).divide(BigDecimal(100))
    }

    @TypeConverter
    fun toLong(bigDecimal: BigDecimal?): Long? {
        return bigDecimal?.multiply(BigDecimal(100))?.toLong()
    }

    @TypeConverter
    fun fromCurrency(value: Currency?): Int {
        return value?.code ?: -1
    }

    @TypeConverter
    fun toCurrency(value: Int): Currency {
        return when (value) {
            Currency.USD.code -> Currency.USD
            Currency.RUB.code -> Currency.RUB
            else -> throw IllegalArgumentException("Not found Currency")
        }
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType?): Int {
        return value?.code ?: -1
    }

    @TypeConverter
    fun toTransactionType(value: Int): TransactionType {
        return when (value) {
            TransactionType.INCOME.code -> TransactionType.INCOME
            TransactionType.OUTCOME.code -> TransactionType.OUTCOME
            else -> throw IllegalArgumentException("Not found TypeTransaction")
        }
    }

    @TypeConverter
    fun fromPeriodicity(value: Periodicity?): Int {
        return value?.code ?: -1
    }

    @TypeConverter
    fun toPeriodicity(value: Int): Periodicity {
        return when (value) {
            Periodicity.OnceMonth.code -> Periodicity.OnceMonth
            Periodicity.Without.code -> Periodicity.Without
            else -> throw IllegalArgumentException("Not found Periodicity")
        }
    }
}