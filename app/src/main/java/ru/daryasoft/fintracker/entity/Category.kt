package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Категория доходов/расходов.
 */
@Entity
data class Category (
        var name: String,
        var transactionType: TransactionType,
        @Ignore
        val iconUri: String,
        @PrimaryKey(autoGenerate = true)
        var idKeyCategory: Long? = null
){
    constructor(): this("", TransactionType.OUTCOME, "")
}