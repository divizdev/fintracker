package ru.daryasoft.fintracker.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.balance.BalanceFragment
import ru.daryasoft.fintracker.transaction.ui.TransactionsFragment

/**
 *  Адаптер для переключения фрагментов на главной активити.
 */
class MainFragmentPagerAdapter(private val activity: FragmentActivity?, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> BalanceFragment.newInstance()
            1 -> TransactionsFragment.newInstance()
            else -> throw IllegalArgumentException()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> activity?.getString(R.string.title_fragment_balance)
                    ?: throw IllegalArgumentException()
            1 -> activity?.getString(R.string.title_fragment_operation)
                    ?: throw IllegalArgumentException()
            else -> throw IllegalArgumentException()
        }
    }
}