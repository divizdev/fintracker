package ru.daryasoft.fintracker.initialization

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.daryasoft.fintracker.account.data.AccountRepository
import ru.daryasoft.fintracker.account.data.AccountRepositoryDB
import ru.daryasoft.fintracker.account.ui.AccountsActivity
import ru.daryasoft.fintracker.account.ui.AddAccountDialogFragment
import ru.daryasoft.fintracker.account.viewModel.AccountsViewModel
import ru.daryasoft.fintracker.balance.BalanceFragment
import ru.daryasoft.fintracker.balance.BalanceViewModel
import ru.daryasoft.fintracker.calculator.TransactionCalculationService
import ru.daryasoft.fintracker.calculator.TransactionCalculationServiceImpl
import ru.daryasoft.fintracker.category.data.CategoryRepository
import ru.daryasoft.fintracker.category.data.CategoryRepositoryDB
import ru.daryasoft.fintracker.category.viewModel.CategoriesViewModel
import ru.daryasoft.fintracker.common.ViewModelFactory
import ru.daryasoft.fintracker.main.MainActivity
import ru.daryasoft.fintracker.rate.RateRepository
import ru.daryasoft.fintracker.rate.RateRepositoryImpl
import ru.daryasoft.fintracker.transaction.data.TransactionRepository
import ru.daryasoft.fintracker.transaction.data.TransactionRepositoryDB
import ru.daryasoft.fintracker.transaction.ui.AddTransactionActivity
import ru.daryasoft.fintracker.transaction.ui.TransactionsFragment
import ru.daryasoft.fintracker.transaction.viewModel.TransactionsViewModel
import javax.inject.Singleton

/**
 * Dagger-модуль с поставщиками зависимостей.
 */
@Module
interface MainBindModule {

    @ContributesAndroidInjector
    fun contributeMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    fun contributeMainFragmentInjector(): BalanceFragment

    @ContributesAndroidInjector
    fun contributeTransactionsFragmentInjector(): TransactionsFragment

    @ContributesAndroidInjector
    fun contributeAccountsFragmentInjector(): AccountsActivity

    @ContributesAndroidInjector
    fun contributeAddTransactionFragmentInjector(): AddTransactionActivity

    @ContributesAndroidInjector
    fun contributeAddAccountDialogFragmentInjector(): AddAccountDialogFragment

    @Binds
    @Singleton
    fun bindFinCalculator(transactionCalculationServiceImpl: TransactionCalculationServiceImpl): TransactionCalculationService

    @Binds
    @Singleton
    fun bindTransactionRepository(transactionRepository: TransactionRepositoryDB): TransactionRepository

    @Binds
    @Singleton
    fun bindAccountRepository(accountRepository: AccountRepositoryDB): AccountRepository

    @Binds
    @Singleton
    fun bindRateRepository(rateRepository: RateRepositoryImpl): RateRepository

    @Binds
    @Singleton
    fun bindCategoryRepository(categoryRepository: CategoryRepositoryDB): CategoryRepository

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BalanceViewModel::class)
    fun postBalanceViewModel(viewModel: BalanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    fun postTransactionsViewModel(viewModel: TransactionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountsViewModel::class)
    fun postAccountsViewModel(viewModel: AccountsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun postCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel
}
