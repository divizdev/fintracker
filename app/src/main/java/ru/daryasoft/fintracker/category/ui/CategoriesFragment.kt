package ru.daryasoft.fintracker.category.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_categories.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.category.adapter.CategoryListAdapter
import ru.daryasoft.fintracker.category.viewModel.CategoriesViewModel
import ru.daryasoft.fintracker.common.INoticeDialogListener
import ru.daryasoft.fintracker.common.Router
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Category
import javax.inject.Inject

/**
 * Фрагмент для отображения категорий.
 */
class CategoriesFragment : DaggerFragment(), INoticeDialogListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var router: Router

    private val viewModel: CategoriesViewModel by lazy { getViewModel<CategoriesViewModel>(viewModelFactory) }
    private val _observer: Observer<List<Category>> by lazy {
        Observer<List<Category>> { list -> categoryListAdapter.setData(list ?: listOf()) }
    }

    private val observerCancelDelete: Observer<Int> by lazy {
        Observer<Int> { it -> it?.let { categoryListAdapter.notifyItemChanged(it) } }
    }

    private val categoryListAdapter = CategoryListAdapter(listOf()) { position -> onDeleteCategory(position) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        categories_recycler_view.layoutManager = linearLayoutManager
        categories_recycler_view.adapter = categoryListAdapter
        add_category.setOnClickListener {
            val appActivity = activity
            if (appActivity is AppCompatActivity) {
                router.navToAddCategories(appActivity)
            }
        }

        categories_recycler_view.addItemDecoration(DividerItemDecoration(categories_recycler_view.context, linearLayoutManager.orientation))
        activity?.title = getString(R.string.title_fragment_categories)
    }

    override fun onStart() {
        super.onStart()
        viewModel.categories.observe(this, _observer)
        viewModel.positionCancelDelete.observe(this, observerCancelDelete)

        viewModel.showDialogDelete.observe(this, Observer {
            showDialogDelete()
        })
    }

    override fun onStop() {
        viewModel.categories.removeObserver(_observer)
        viewModel.positionCancelDelete.removeObserver(observerCancelDelete)
        super.onStop()
    }

    private fun showDialogDelete() {
        val dialog = DeleteCategoryDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager, "")
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
