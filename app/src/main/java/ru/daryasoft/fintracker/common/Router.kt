package ru.daryasoft.fintracker.common

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.account.ui.AccountsActivity
import ru.daryasoft.fintracker.category.ui.AddCategoryFragment
import ru.daryasoft.fintracker.category.ui.CategoriesFragment
import ru.daryasoft.fintracker.main.MainFragment
import ru.daryasoft.fintracker.transaction.ui.AddTransactionFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {
    fun navToAddTransaction(activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddTransactionFragment.newInstance(-1))
                .addToBackStack(null)
                .commit()
    }

    fun navToEditTransaction(activity: AppCompatActivity, idTransaction: Long){
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddTransactionFragment.newInstance(idTransaction))
                .addToBackStack(null)
                .commit()
    }

    fun navToCategory(activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, CategoriesFragment())
                .addToBackStack(null)
                .commit()
    }

    fun navToAccountsActivity(activity: AppCompatActivity) {
        val intent = Intent(activity, AccountsActivity::class.java)
        activity.startActivity(intent)
    }

    fun navToAddCategories(activity: AppCompatActivity){

        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, AddCategoryFragment())
                .addToBackStack(null)
                .commit()

    }

    fun navToHomeFragment(activity: AppCompatActivity){
        activity.supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, MainFragment.newInstance()).commit()
    }
}