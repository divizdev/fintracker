package ru.daryasoft.fintracker.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Расширения для ui-классов.
 */
fun FragmentActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
}
fun FragmentActivity.replaceFragmentAndBack(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]

fun AppCompatActivity.hideKeyboard(view: View) {
    ContextCompat.getSystemService(this, InputMethodManager::class.java)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
}
