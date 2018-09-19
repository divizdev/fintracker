package ru.daryasoft.fintracker.calculator

import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Сервис для выполнения финансовых расчетов.
 * Нужна полная переделека, так как доставшийся алгоритм опирается на "Асинхронную" LiveData
 */
class TransactionCalculationServiceImpl @Inject constructor(private val transactionRepository: TransactionRepository) : TransactionCalculationService {
    override fun aggregateByCategories(account: Account?, categories: List<Category>, targetCurrency: Currency): List<TransactionAggregateInfo> {
        val result = mutableListOf<TransactionAggregateInfo>()

        for (category in categories) {
            var sum = BigDecimal.ZERO
            val transactions = transactionRepository.query(category, account).value
            for (transaction in transactions ?: listOf()) {
                sum = sum.add (if (transaction.category.transactionType == TransactionType.INCOME) transaction.sum.value else -transaction.sum.value)
            }
            result.add(TransactionAggregateInfo(Money(sum, targetCurrency), category))
        }

        return result
    }
}