package ru.daryasoft.fintracker.account.ui

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.dialog_add_account.view.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.account.viewModel.AccountsViewModel
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Account
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.Money
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Диалог, который отображается при создании счета.
 */
class AddAccountDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AccountsViewModel by lazy { getViewModel<AccountsViewModel>(viewModelFactory) }

    private val dialogView by lazy { activity?.layoutInflater?.inflate(R.layout.dialog_add_account, null) }
    private val dialog by lazy {
        AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle(getString(R.string.adding_account_title))
                .setPositiveButton(getString(R.string.adding_account_positive_button_title)) { _, _ -> viewModel.onAddAccount(onAddAccount()) }
                .setNegativeButton(getString(R.string.adding_account_negative_button_title)) { _, _ -> }
                .create()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView?.account_currency_spinner?.adapter = ArrayAdapter<Currency>(context, android.R.layout.simple_spinner_item, Currency.values())

        dialogView?.account_name?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = text != null && text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = dialogView?.account_name?.text?.isNotEmpty() ?: false
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun onAddAccount(): Account {
        val accountName = dialogView?.account_name?.text.toString()
        val startAmount = dialogView?.account_start_amount?.text.toString()
        val currency = dialogView?.account_currency_spinner?.selectedItem as Currency
        return Account(accountName, Money(if (startAmount.isEmpty()) BigDecimal.ZERO else startAmount.toBigDecimal(), currency))
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddAccountDialogFragment()
    }
}