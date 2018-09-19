package ru.daryasoft.fintracker.account.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_accounts.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.account.adapter.AccountListAdapter
import ru.daryasoft.fintracker.account.viewModel.AccountsViewModel
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Account
import javax.inject.Inject

/**
 * Фрагмент для счетов.
 */
class AccountsActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AccountsViewModel by lazy { getViewModel<AccountsViewModel>(viewModelFactory) }
    private val observer: Observer<List<Account>> by lazy {
        Observer<List<Account>> { list -> accountListAdapter.setData(list ?: listOf()) }
    }

    private val accountListAdapter = AccountListAdapter(listOf()) { position -> onDeleteAccount(position) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val supportActionBar = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.title_fragment_accounts)

        account_list.layoutManager = LinearLayoutManager(this)
        account_list.adapter = accountListAdapter

        add_account.setOnClickListener { onAddAccount() }

    }

    override fun onStart() {
        super.onStart()
        viewModel.accounts.observe(this, observer)
    }

    override fun onStop() {
        viewModel.accounts.removeObserver(observer)
        super.onStop()
    }

    private fun onAddAccount() {
        AddAccountDialogFragment.newInstance().show(supportFragmentManager, "AddAccountDialogFragment")
    }

    private fun onDeleteAccount(position: Int) {
        OnDeleteAccountDialogFragment.newInstance { viewModel.onDeleteAccount(position) }
                .show(supportFragmentManager, "OnDeleteAccountDialogFragment")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
