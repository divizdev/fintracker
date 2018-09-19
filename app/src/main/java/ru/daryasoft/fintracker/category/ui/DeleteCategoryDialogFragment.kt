package ru.daryasoft.fintracker.category.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.INoticeDialogListener

/**
 * Диалог, который отображается для подтверждения удаления счета.
 */
class DeleteCategoryDialogFragment : DialogFragment() {

    var listener: INoticeDialogListener? = null




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
                .setTitle(R.string.title_request_delete_item_category)
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