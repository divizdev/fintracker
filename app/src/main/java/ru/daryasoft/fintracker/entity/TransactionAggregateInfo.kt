package ru.daryasoft.fintracker.entity

/**
 * Агрегированная информация по множеству транзакций.
 */
data class TransactionAggregateInfo(val amount: Money, val category: Category)