package ru.daryasoft.fintracker.transaction.adapter

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat.getDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.transaction_item.view.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.LocaleUtils
import ru.daryasoft.fintracker.entity.TransactionType
import ru.daryasoft.fintracker.entity.TransactionUI


/**
 * Адаптер для отображения списка транзакций.
 */
class TransactionListAdapter(private var transactionDBS: List<TransactionUI>,
                             private val onDeleteAction: (position: Int) -> Unit) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionDBS[position]
        holder.setData(transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return transactionDBS.size
    }

    fun setData(changedTransactionDBS: List<TransactionUI>) {
        transactionDBS = changedTransactionDBS
        notifyDataSetChanged() //Добавить DiffUtil
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val localeUtils = LocaleUtils(itemView.context)
        init {
            itemView.setOnLongClickListener {
                onDeleteAction.invoke(adapterPosition)
                true
            }
        }

        fun setData(transactionDB: TransactionUI) {
//            val uri = Uri.parse("android.resource://ru.daryasoft.fintracker/drawable/" + transactionDB.category.iconUri)

//            itemView.category_icon.setImageURI(uri)
            itemView.transaction_type.setImageDrawable(itemView.context.resources.getDrawable(getIconForTransactionType(transactionDB.category.transactionType)))
            itemView.transaction_sum.text = localeUtils.formatBigDecimal( transactionDB.sum.value)
            itemView.transaction_currency.text =  localeUtils.formatCurrency(transactionDB.sum.currency)
            itemView.transaction_date.text = getDateFormat(itemView.context).format(transactionDB.date)
            itemView.transaction_date.text = transactionDB.nameAccount
        }

        private fun getIconForTransactionType(transactionType: TransactionType): Int {
            return when (transactionType) {
                TransactionType.OUTCOME -> R.drawable.ic_chevron_left_black_24dp
                TransactionType.INCOME -> R.drawable.ic_chevron_right_black_24dp
            }
        }
    }
}