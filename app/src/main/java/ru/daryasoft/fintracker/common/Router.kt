package ru.daryasoft.fintracker.common

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import ru.daryasoft.fintracker.account.ui.AccountsActivity
import ru.daryasoft.fintracker.category.ui.CategoriesActivity
import ru.daryasoft.fintracker.transaction.ui.AddTransactionActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {
    fun navToAddTransaction(activity: AppCompatActivity) {
        val intent = Intent(activity, AddTransactionActivity::class.java)
        activity.startActivity(intent)
    }

    fun navToCategory(activity: AppCompatActivity) {
        val intent = Intent(activity, CategoriesActivity::class.java)
        activity.startActivity(intent)
    }

    fun navToAccountsActivity(activity: AppCompatActivity) {
        val intent = Intent(activity, AccountsActivity::class.java)
        activity.startActivity(intent)
    }
}