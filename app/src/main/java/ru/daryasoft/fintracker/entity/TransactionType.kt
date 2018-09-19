package ru.daryasoft.fintracker.entity

import ru.daryasoft.fintracker.R

/**
 * Тип финансовой транзакции.
 */
enum class TransactionType(val resId: Int, val code: Int) {
    INCOME(R.string.income, 0), OUTCOME(R.string.outcome, 1);
}