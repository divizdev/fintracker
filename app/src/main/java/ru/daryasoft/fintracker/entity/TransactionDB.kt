package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

/**
 * Финансовая транзакция.
 */
@Entity//(foreignKeys = arrayOf(
//        ForeignKey(entity = Account::class,
//                parentColumns = arrayOf("id"),
//                childColumns = arrayOf("idAccount"),
//                onDelete = ForeignKey.CASCADE)))
data class TransactionDB(
        @Ignore
        @Embedded
        var account: Account, //В будущем необходимо удалить
        @Embedded
        var sum: Money,
        var date: Date,
        @Ignore
        @Embedded
        var category: Category,
        var periodicity: Periodicity = Periodicity.Without,
        var isScheduled: Boolean = true,
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null,
        var idAccount: Long? = account.id,
        var idCategory: Long? = category.idKeyCategory
) {
    constructor() : this(Account("", Money(BigDecimal.ZERO, Currency.USD)), Money(BigDecimal.ZERO, Currency.USD), Date(), Category("", TransactionType.INCOME, ""))
}