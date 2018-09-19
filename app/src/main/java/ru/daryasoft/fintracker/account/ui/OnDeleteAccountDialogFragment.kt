package ru.daryasoft.fintracker.account.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

/**
 * Диалог, который отображается для подтверждения удаления счета.
 */
class OnDeleteAccountDialogFragment : DialogFragment() {
    private lateinit var onDeleteAccount: () -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
                .setTitle("Вы действительно хотите удалить счет?")
                .setPositiveButton("OK") { _, _ -> onDeleteAccount.invoke() }
                .setNegativeButton("Cancel") { _, _ -> this@OnDeleteAccountDialogFragment.dialog.cancel() }
        return dialogBuilder.create()
    }

    companion object {
        @JvmStatic
        fun newInstance(onDeleteAccount: () -> Unit): OnDeleteAccountDialogFragment {
            val onDeleteAccountDialogFragment = OnDeleteAccountDialogFragment()
            onDeleteAccountDialogFragment.onDeleteAccount = onDeleteAccount
            return onDeleteAccountDialogFragment
        }
    }
}