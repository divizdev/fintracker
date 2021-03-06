package ru.daryasoft.fintracker.transaction.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.INoticeDialogListener

/**
 * Диалог, который отображается для подтверждения удаления счета.
 */
class DeleteTransactionDialogFragment : DialogFragment() {

    var listener: INoticeDialogListener? = null


    private lateinit var onDeleteTransaction: () -> Unit
    private lateinit var onCancelDelete: () -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
                .setTitle(R.string.title_request_delete_item_transaction)
                .setPositiveButton(android.R.string.ok) { _, _ -> listener?.onDialogOk() }
                .setNegativeButton(android.R.string.cancel) { _, _ -> listener?.onDialogCancel() }
        return dialogBuilder.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (targetFragment is INoticeDialogListener) {
            listener = targetFragment as INoticeDialogListener

        } else {
            throw RuntimeException(targetFragment!!.toString() + " must implement INoticeDialogListener")
        }
    }

}