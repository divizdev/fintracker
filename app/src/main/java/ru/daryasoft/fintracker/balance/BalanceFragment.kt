package ru.daryasoft.fintracker.balance

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_balance.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.CustomArrayAdapter
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Account
import ru.daryasoft.fintracker.entity.Balance
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.TransactionAggregateInfo
import javax.inject.Inject
import kotlin.math.absoluteValue

private const val CURRENCY_POSITION_KEY = "currencyPosition"
private const val ACCOUNT_POSITION_KEY = "accountPosition"

/**
 * Главный фрагмент (содержащий баланс пользователя).
 */
class BalanceFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val balanceViewModel: BalanceViewModel by lazy { getViewModel<BalanceViewModel>(viewModelFactory) }

    private val balanceObserver: Observer<Balance> by lazy {
        Observer<Balance> {
            balance_value.text = String.format("%.2f", it?.sum)
            balance_currency.text = it?.currency.toString()
        }
    }

    private val currencyObserver: Observer<Currency> by lazy {
        Observer<Currency> {
            currency_spinner.setSelection(it?.ordinal ?: 0)
        }
    }

    private val transactionAggregateInfoObserver: Observer<List<TransactionAggregateInfo>> by lazy {
        Observer<List<TransactionAggregateInfo>> { initPieChart(it ?: listOf()) }
    }

    private val accountObserver: Observer<List<Account>> by lazy {
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
        currencyPosition = savedInstanceState?.getInt(CURRENCY_POSITION_KEY)
        accountPosition = savedInstanceState?.getInt(ACCOUNT_POSITION_KEY)

        initCurrencySpinner()
        initAccountSpinner()
    }

    override fun onStart() {
        super.onStart()

        balanceViewModel.transactionAggregateInfoList.observe(this@BalanceFragment, transactionAggregateInfoObserver)
        balanceViewModel.balance.observe(this@BalanceFragment, balanceObserver)
        balanceViewModel.currency.observe(this@BalanceFragment, currencyObserver)
        balanceViewModel.accounts.observe(this@BalanceFragment, accountObserver)

        if (accountPosition != null) {
            account_spinner.setSelection(accountPosition as Int)
        }
        if (currencyPosition != null) {
            currency_spinner.setSelection(currencyPosition as Int)
        }
    }

    override fun onStop() {
        balanceViewModel.transactionAggregateInfoList.removeObserver(transactionAggregateInfoObserver)
        balanceViewModel.balance.removeObserver(balanceObserver)
        balanceViewModel.currency.removeObserver(currencyObserver)
        balanceViewModel.accounts.removeObserver(accountObserver)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENCY_POSITION_KEY, currency_spinner.selectedItemPosition)
        outState.putInt(ACCOUNT_POSITION_KEY, account_spinner.selectedItemPosition)
        super.onSaveInstanceState(outState)
    }

    private fun initAccountSpinner() {
        account_spinner.adapter = CustomArrayAdapter(context, balanceViewModel.accounts.value
                ?: listOf()) { account -> account.name }

        account_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                balanceViewModel.onChangeAccount(account_spinner.selectedItem as Account)
            }
        }
    }

    private fun initCurrencySpinner() {
        currency_spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Currency.values().map { it.toString() })

        currency_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                balanceViewModel.onChangeCurrency(Currency.values()[position])
            }
        }
    }

    private fun initPieChart(transactionAggregateInfoList: List<TransactionAggregateInfo>) {
        val entries = mutableListOf<PieEntry>()
        val sum = transactionAggregateInfoList.map { it.amount.absoluteValue }.sum()
        for (transactionAggregateInfo in transactionAggregateInfoList) {
            val categoryWasteAmount = (transactionAggregateInfo.amount.absoluteValue / sum * 100).toFloat()
            if (categoryWasteAmount > 0) {
                entries.add(PieEntry(categoryWasteAmount, transactionAggregateInfo.category.name))
            }
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())

        balance_diagram.data = data
        balance_diagram.legend.isEnabled = false
        balance_diagram.notifyDataSetChanged()
        balance_diagram.invalidate()
    }

    companion object {
        @JvmStatic
        fun newInstance() = BalanceFragment()
    }
}
