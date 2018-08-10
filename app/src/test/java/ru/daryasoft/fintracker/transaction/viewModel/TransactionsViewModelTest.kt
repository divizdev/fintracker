package ru.daryasoft.fintracker.transaction.viewModel


import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.Mockito.mock
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import java.math.BigDecimal
import java.util.*

class TransactionsViewModelTest {

    lateinit var transactionRepository: TransactionRepository

    @Test
    fun onUpdateTransaction() {
        transactionRepository = mock(TransactionRepository::class.java)

        val transactionsViewModel = TransactionsViewModel(transactionRepository)
        val account = Account("", Money(BigDecimal.valueOf(10), Currency.RUB), 2)
        val date = Date()
        val transactionDBOld = TransactionDB(Account(), Money(BigDecimal.valueOf(10), Currency.RUB), date, Category("", TransactionType.INCOME))
        val transactionDBNew = TransactionDB(Account(), Money(BigDecimal.valueOf(10), Currency.RUB), date, Category("", TransactionType.OUTCOME))
        transactionsViewModel.onUpdateTransaction(account, transactionDBOld, transactionDBNew, Category())

        verify(transactionRepository).add(transactionDBNew, Account("", Money(BigDecimal.valueOf(-10), Currency.RUB), 2))

    }
}