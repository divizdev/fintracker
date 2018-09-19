package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import java.math.BigDecimal
import java.util.*

data class TransactionUI(
        @Embedded

        var sum: Money,
        @ColumnInfo(name = "date")
        var date: Date,

        @Embedded
        var category: Category,
        @ColumnInfo
        var nameAccount: String = "",
        @ColumnInfo
        var sumAccount: BigDecimal = BigDecimal.ZERO,
        @ColumnInfo(name = "id")
        var id: Long? = null,
        @ColumnInfo(name = "idAccount")
        var idAccount: Long? = null,
        @ColumnInfo(name = "idCategory")
        var idCategory: Long? = category.idKeyCategory

) {
    constructor() : this(Money(), Date(), Category())
}