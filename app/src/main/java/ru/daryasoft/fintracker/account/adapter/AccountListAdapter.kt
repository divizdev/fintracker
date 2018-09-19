package ru.daryasoft.fintracker.account.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.account_item.view.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.LocaleUtils
import ru.daryasoft.fintracker.entity.Account

/**
 * Адаптер для отображения списка счетов.
 */
class AccountListAdapter(private var accounts: List<Account>,
                         private val onDeleteAction: (position: Int) -> Unit) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.setData(accounts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false))
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    fun setData(changedAccounts: List<Account>) {
        accounts = changedAccounts
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val localeUtils = LocaleUtils(itemView.context)
        fun setData(account: Account){
            itemView.accountName.text = account.name
            itemView.accountBalance.text = localeUtils.formatBigDecimal(account.money.value)
            itemView.accountCurrency.text = localeUtils.formatCurrency(account.money.currency)
        }
        init {
            itemView.setOnLongClickListener {
                onDeleteAction.invoke(adapterPosition)
                true
            }
        }
    }
}