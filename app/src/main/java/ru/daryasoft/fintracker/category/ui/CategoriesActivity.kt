package ru.daryasoft.fintracker.category.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_categories.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.category.adapter.CategoryListAdapter
import ru.daryasoft.fintracker.category.viewModel.CategoriesViewModel
import ru.daryasoft.fintracker.common.INoticeDialogListener
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Category
import javax.inject.Inject

/**
 * Фрагмент для отображения категорий.
 */
class CategoriesActivity : DaggerAppCompatActivity(), INoticeDialogListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CategoriesViewModel by lazy { getViewModel<CategoriesViewModel>(viewModelFactory) }
    private val _observer: Observer<List<Category>> by lazy {
        Observer<List<Category>> { list -> categoryListAdapter.setData(list ?: listOf()) }
    }

    private val observerCancelDelete: Observer<Int> by lazy {
        Observer<Int> { it -> it?.let { categoryListAdapter.notifyItemChanged(it) } }
    }

    private val categoryListAdapter = CategoryListAdapter(listOf()) { position -> onDeleteCategory(position) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        title = getString(R.string.title_fragment_categories)

        val supportActionBar = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val linearLayoutManager = LinearLayoutManager(this)
        categories_recycler_view.layoutManager = linearLayoutManager
        categories_recycler_view.adapter = categoryListAdapter

        categories_recycler_view.addItemDecoration(DividerItemDecoration(categories_recycler_view.context, linearLayoutManager.orientation))

        viewModel.categories.observe(this, _observer)
        viewModel.positionCancelDelete.observe(this, observerCancelDelete)

        viewModel.showDialogDelete.observe(this, Observer {
            showDialogDelete()
        })


    }


    private fun showDialogDelete() {
        val dialog = DeleteCategoryDialogFragment()

        dialog.show(supportFragmentManager, "")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onDeleteCategory(position: Int) {
        viewModel.onDeleteCategory(position)
    }

    override fun onDialogOk() {
        viewModel.onConfirmDeleteCategory()
    }

    override fun onDialogCancel() {
        viewModel.onCancelDeleteCategory()
    }


}
