package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.math.BigDecimal

/**
 * Счет.
 */
@Entity
data class Account(
        var name: String,
        @Embedded
        var money: Money,
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null

){
    constructor(): this("", Money(BigDecimal.ZERO, Currency.RUB))
}