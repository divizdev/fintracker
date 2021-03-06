package ru.daryasoft.fintracker.main

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.common.Router
import ru.daryasoft.fintracker.common.replaceFragment
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

                } else {
                    drawer_layout.openDrawer(Gravity.START)
                }
//                syncActionBarArrowState()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    lateinit var toggle: ActionBarDrawerToggle

    private fun initSideMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.title_fragment_balance, R.string.title_fragment_operation)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
            // Handle home button in non-drawer mode
                android.R.id.home -> onBackPressed()
                R.id.main_page -> router.navToHomeFragment(this)
                R.id.accounts -> router.navToAccountsActivity(this)
                R.id.categories -> router.navToCategory(this)
//                R.id.settings -> replaceFragmentAndBack(SettingsFragment.newInstance(), R.id.main_fragment_container)
                R.id.about -> router.navToAbout(this)
                else -> throw IllegalArgumentException()
            }

//            syncActionBarArrowState()
            true
        }

        supportFragmentManager.addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener {
            syncActionBarArrowState()
        }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun syncActionBarArrowState() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        toggle.isDrawerIndicatorEnabled = backStackEntryCount == 0
//        supportActionBar!!.setDisplayHomeAsUpEnabled(backStackEntryCount != 0)
    }
}
