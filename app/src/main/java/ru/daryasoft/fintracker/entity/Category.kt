package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Категория доходов/расходов.
 */
@Entity
data class Category (
        var name: String,
        var transactionType: TransactionType,
        @PrimaryKey(autoGenerate = true)
        var idKeyCategory: Long? = null
){
    constructor(): this("", TransactionType.OUTCOME )
}