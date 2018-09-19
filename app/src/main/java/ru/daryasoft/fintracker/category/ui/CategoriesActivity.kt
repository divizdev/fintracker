package ru.daryasoft.fintracker.category.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.daryasoft.fintracker.R

/**
 * Фрагмент для отображения категорий.
 */
class CategoriesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        title = getString(R.string.title_fragment_categories)

        val supportActionBar = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}
