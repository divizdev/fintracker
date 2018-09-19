package ru.daryasoft.fintracker.main

import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.Router
import ru.daryasoft.fintracker.common.replaceFragment
import ru.daryasoft.fintracker.common.replaceFragmentAndBack
import ru.daryasoft.fintracker.settings.SettingsFragment
import ru.daryasoft.fintracker.transaction.ui.AddTransactionListener
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), AddTransactionListener {


    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSideMenu()

        if (savedInstanceState == null) {
            replaceFragment(MainFragment.newInstance(), R.id.main_fragment_container)
        }
    }

    override fun onAddTransactionOpen() {
        router.navToAddTransaction(this)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawer_layout.isDrawerOpen(nav_view)) {
                    drawer_layout.closeDrawer(Gravity.START)
                }
                else {
                    drawer_layout.openDrawer(Gravity.START)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSideMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.title_fragment_balance, R.string.title_fragment_operation)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
                R.id.main_page -> replaceFragment(MainFragment.newInstance(), R.id.main_fragment_container)
                R.id.accounts -> router.navToAccountsActivity(this)
                R.id.categories -> router.navToCategory(this)
                R.id.settings -> replaceFragmentAndBack(SettingsFragment.newInstance(), R.id.main_fragment_container)
                else -> throw IllegalArgumentException()
            }

            true
        }
    }
}
