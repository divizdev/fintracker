package ru.daryasoft.fintracker.transaction.ui

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.account.viewModel.AccountsViewModel
import ru.daryasoft.fintracker.category.viewModel.CategoriesViewModel
import ru.daryasoft.fintracker.common.CustomArrayAdapter
import ru.daryasoft.fintracker.common.LocaleUtils
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.common.hideKeyboard
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.transaction.viewModel.TransactionsViewModel
import java.util.*
import javax.inject.Inject


private const val ID_TRANSACTION = "idTransaction"

class AddTransactionFragment : DaggerFragment() {

    private var idTransaction: Long = -1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var localeUtils: LocaleUtils

    private val transactionsViewModel: TransactionsViewModel by lazy { getViewModel<TransactionsViewModel>(viewModelFactory) }
    private val categoriesViewModel: CategoriesViewModel by lazy { getViewModel<CategoriesViewModel>(viewModelFactory) }
    private val accountsViewModel: AccountsViewModel by lazy { getViewModel<AccountsViewModel>(viewModelFactory) }
    private val accountsObserver: android.arch.lifecycle.Observer<List<Account>> by lazy {
        android.arch.lifecycle.Observer<List<Account>> {
            listAccount.clear()
            listAccount.addAll(it ?: listOf())
            (transaction_account_spinner.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        }
    }
    private val categoryObserver: android.arch.lifecycle.Observer<List<Category>> by lazy {
        android.arch.lifecycle.Observer<List<Category>> {
            listCategories.clear()
            listCategories.addAll(it ?: listOf())
            (category_spinner.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        }
    }

    private val transactionObserver: android.arch.lifecycle.Observer<TransactionDB> by lazy {
        android.arch.lifecycle.Observer<TransactionDB> { it ->
            it?.let {
                transaction_amount.setText(it.sum.value.toString())
                transaction_date_selector.text = DateFormat.getDateFormat(activity).format(it.date)

                for ((index, item) in listAccount.withIndex()) {
                    if (item.id == it.idAccount) {
                        transaction_account_spinner.setSelection(index)
                    }
                }

                for ((index, item) in listCategories.withIndex()) {
                    if (item.idKeyCategory == it.idCategory) {
                        category_spinner.setSelection(index)
                    }
                }


            }
        }
    }

    private var listAccount: MutableList<Account> = mutableListOf()
    private var listCategories: MutableList<Category> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.title_fragment_add_transaction)

        arguments?.let {
            idTransaction = it.getLong(ID_TRANSACTION)
        }

        localeUtils = LocaleUtils(activity)

        initTransactionTypeSwitcher()
        initTransactionDateSelector()
        initTransactionPeriodicitySwitcher()
        initAccountSpinner()
        initCategorySpinner()
        initOkButton()

        if (idTransaction != -1L) {
            transactionsViewModel.getTransaction(idTransaction).observe(this, transactionObserver)
        }
    }


    private fun initTransactionTypeSwitcher() {
        transaction_type_spinner.adapter = CustomArrayAdapter(activity,
                TransactionType.values().toList()) { transactionType -> getString(transactionType.resId) }

        transaction_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                categoriesViewModel.getCategoriesByType(transaction_type_spinner.selectedItem as TransactionType).observe(this@AddTransactionFragment, categoryObserver)
            }
        }
    }

    private fun initTransactionPeriodicitySwitcher() {
        spinner_periodicity.adapter = CustomArrayAdapter(activity,
                Periodicity.values().toList()) { transactionType -> getString(transactionType.resId) }

    }

    private fun initTransactionDateSelector() {
        transaction_date_selector.text = DateFormat.getDateFormat(activity).format(Date())

        transaction_date_selector.setOnClickListener {
            val currentDate = Calendar.getInstance()
            DatePickerDialog(activity,
                    DatePickerDialog.OnDateSetListener { p0, year, month, dayOfMonth ->
                        val date = Calendar.getInstance()
                        date.set(Calendar.YEAR, year)
                        date.set(Calendar.MONTH, month)
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        transaction_date_selector.text = DateFormat.getDateFormat(activity).format(date.time)
                    },
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH))
                    .show()
        }
    }


    private fun initAccountSpinner() {
        transaction_account_spinner.adapter = CustomArrayAdapter<Account>(activity, listAccount) { account -> account.name }
        transaction_account_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapter: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                transaction_currency.text = (transaction_account_spinner.selectedItem as Account).money.currency.name
            }
        }

        accountsViewModel.accounts.observe(this, accountsObserver)
    }

    private fun initCategorySpinner() {
        category_spinner.adapter = CustomArrayAdapter(activity, listCategories)
        { category -> category.name }

        categoriesViewModel.categories.observe(this, categoryObserver)
    }

    private fun initOkButton() {
        transaction_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                add_transaction_ok.isEnabled = text != null && text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        add_transaction_ok.setOnClickListener {
            val account = transaction_account_spinner.selectedItem as Account
            val transactionSum = transaction_amount.text.toString().toDouble()
            val date = Date()
            val category = category_spinner.selectedItem as Category
            val periodicity = spinner_periodicity.selectedItem as Periodicity
            val transaction = TransactionDB(account, Money(transactionSum.toBigDecimal(), account.money.currency), date, category, periodicity)
            if (idTransaction != -1L) {
                transaction.id = idTransaction
            }

            transactionsViewModel.onAddTransaction(account, transaction, category)
            hideKeyboard(transaction_amount)
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(idTransaction: Long?): AddTransactionFragment = AddTransactionFragment()
                .apply {
                    arguments = Bundle().apply {
                        if (idTransaction != null)
                            putLong(ID_TRANSACTION, idTransaction)
                        else {
                            putLong(ID_TRANSACTION, -1L)
                        }
                    }
                }
    }


}
