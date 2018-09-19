package ru.daryasoft.fintracker.balance

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_balance.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.CustomArrayAdapter
import ru.daryasoft.fintracker.common.LocaleUtils
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Account
import ru.daryasoft.fintracker.entity.Balance
import ru.daryasoft.fintracker.entity.Currency
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Главный фрагмент (содержащий баланс пользователя).
 */
class BalanceFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val localeUtils = LocaleUtils(context)


    private val balanceViewModel: BalanceViewModel by lazy { getViewModel<BalanceViewModel>(viewModelFactory) }

    private val balanceObserver: Observer<Balance> by lazy {
        Observer<Balance> { it ->
            it?.let {
                balance_value.text = localeUtils.formatBigDecimal(it.money.value)
                balance_currency.text = localeUtils.formatCurrency(it.money.currency)
            }
        }
    }


    private val currencyObserver: Observer<Currency> by lazy {
        Observer<Currency> {
            currency_spinner.setSelection(it?.ordinal ?: 0)
        }
    }


    private val accountObserver: Observer<Account> by lazy {
        Observer<Account> {
            account_spinner.setSelection(balanceViewModel.accounts.value?.indexOf(balanceViewModel.account.value)
                    ?: 0)
        }
    }

    private val accountsObserver: Observer<List<Account>> by lazy {
        Observer<List<Account>> {
            account_spinner.adapter = CustomArrayAdapter(context, balanceViewModel.accounts.value
                    ?: listOf()) { account -> account.name }
        }
    }

    private var currencyPosition: Int? = null
    private var accountPosition: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCurrencySpinner()
        initAccountSpinner()
    }

    override fun onStart() {
        super.onStart()


        balanceViewModel.balance.observe(this@BalanceFragment, balanceObserver)
        balanceViewModel.currency.observe(this@BalanceFragment, currencyObserver)
        balanceViewModel.accounts.observe(this@BalanceFragment, accountsObserver)
        balanceViewModel.account.observe(this@BalanceFragment, accountObserver)
    }

    override fun onStop() {

        balanceViewModel.balance.removeObserver(balanceObserver)
        balanceViewModel.currency.removeObserver(currencyObserver)
        balanceViewModel.accounts.removeObserver(accountsObserver)
        balanceViewModel.account.removeObserver(accountObserver)

        super.onStop()
    }

    private fun initAccountSpinner() {
        account_spinner.adapter = CustomArrayAdapter(context, balanceViewModel.accounts.value
                ?: listOf()) { account -> account.name }

        account_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                balanceViewModel.onChangeAccount(account_spinner.selectedItem as Account)
                val currency = (account_spinner.selectedItem as Account).money.currency
                balanceViewModel.getIncome(account_spinner.selectedItem as Account).observe(this@BalanceFragment, Observer {
                    value_revenue_text_view.text = localeUtils.formatBigDecimal(it
                            ?: BigDecimal.ZERO)
                    revenue_currency_text_view.text = localeUtils.formatCurrency(currency)
                })
                balanceViewModel.getOutcome(account_spinner.selectedItem as Account).observe(this@BalanceFragment, Observer {
                    value_expense_text_view.text = localeUtils.formatBigDecimal(it
                            ?: BigDecimal.ZERO)
                    expense_currency_text_view.text = localeUtils.formatCurrency(currency)
                })
            }
        }
    }

    private fun initCurrencySpinner() {

        currency_spinner.adapter = CustomArrayAdapter(activity,
                Currency.values().toList()) { it -> it.toString() }


        currency_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                balanceViewModel.onChangeCurrency(Currency.values()[position])
                balanceViewModel.getIncome(account_spinner.selectedItem as Account).observe(this@BalanceFragment, Observer {

                    value_revenue_text_view.text = localeUtils.formatBigDecimal(balanceViewModel.recalcValue(it
                            ?: BigDecimal.ZERO))
                    revenue_currency_text_view.text = localeUtils.formatCurrency(Currency.values()[position])
                })
                balanceViewModel.getOutcome(account_spinner.selectedItem as Account).observe(this@BalanceFragment, Observer {
                    value_expense_text_view.text = localeUtils.formatBigDecimal(balanceViewModel.recalcValue(it
                            ?: BigDecimal.ZERO))
                    expense_currency_text_view.text = localeUtils.formatCurrency(Currency.values()[position])
                })

            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = BalanceFragment()
    }
}
