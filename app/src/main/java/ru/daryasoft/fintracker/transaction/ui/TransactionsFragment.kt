package ru.daryasoft.fintracker.transaction.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_transactions.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.TransactionUI
import ru.daryasoft.fintracker.transaction.adapter.TransactionItemTouchHelper
import ru.daryasoft.fintracker.transaction.adapter.TransactionListAdapter
import ru.daryasoft.fintracker.transaction.viewModel.TransactionsViewModel
import javax.inject.Inject


/**
 * Фрагмент для списка транзакций.
 */
class TransactionsFragment : DaggerFragment(), DeleteTransactionDialogFragment.INoticeDialogListener {
    override fun onDialogOk() {
        viewModel.onConfirmDeleteTransaction()
    }

    override fun onDialogCancel() {
        viewModel.onCancelDeleteTransaction()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TransactionsViewModel by lazy { getViewModel<TransactionsViewModel>(viewModelFactory) }
    private val _observer: Observer<List<TransactionUI>> by lazy {
        Observer<List<TransactionUI>> { list -> transactionListAdapter.setData(list ?: listOf()) }
    }
    private val observerCancelDelete: Observer<Int> by lazy {
        Observer<Int> { it -> it?.let { transactionListAdapter.notifyItemChanged(it) } }
    }



    private val transactionListAdapter = TransactionListAdapter(listOf()) { position -> onDeleteTransaction(position) }
    private var addTransactionListener: AddTransactionListener? = null

    private val itemTouchHelper = ItemTouchHelper(TransactionItemTouchHelper(0, ItemTouchHelper.LEFT) { position -> onDeleteTransaction(position) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaction_list.layoutManager = LinearLayoutManager(context)
        transaction_list.adapter = transactionListAdapter
        itemTouchHelper.attachToRecyclerView(transaction_list)

        add_transaction.setOnClickListener { addTransactionListener?.onAddTransactionOpen() }
    }

    override fun onStart() {
        super.onStart()
        viewModel._transactions.observe(this, _observer)
        viewModel.positionCancelDelete.observe(this, observerCancelDelete)

        viewModel.showDialogDelete.observe(this, Observer {
            showDialogDelete()
        })

    }

    override fun onStop() {
        viewModel._transactions.removeObserver(_observer)
        viewModel.positionCancelDelete.removeObserver(observerCancelDelete)
        super.onStop()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is AddTransactionListener) {
            throw IllegalArgumentException()
        }
        addTransactionListener = context
    }

    override fun onDetach() {
        addTransactionListener = null
        super.onDetach()
    }

    private fun showDialogDelete() {
        val dialog = DeleteTransactionDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager, TAG_DIALOG_DELETE)
    }

    private fun onDeleteTransaction(position: Int) {
        viewModel.onDeleteTransaction(position)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TransactionsFragment()

        const val TAG_DIALOG_DELETE: String = "DeleteTransactionDialogFragment"
    }
}