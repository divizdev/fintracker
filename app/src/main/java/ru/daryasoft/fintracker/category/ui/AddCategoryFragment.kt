package ru.daryasoft.fintracker.category.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_category.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.category.viewModel.AddCategoryViewModel
import ru.daryasoft.fintracker.common.CustomArrayAdapter
import ru.daryasoft.fintracker.common.getViewModel
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType
import javax.inject.Inject


class AddCategoryFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AddCategoryViewModel by lazy { getViewModel<AddCategoryViewModel>(viewModelFactory) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTransactionTypeSwitcher()
        save_category_button.setOnClickListener {
            if (!value_edit_text.text.isEmpty()) {
                viewModel.onAddCategory(Category(value_edit_text.text.toString(), type_operation_spinner.selectedItem as TransactionType))
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_category, container, false)

    }

    private fun initTransactionTypeSwitcher() {
        type_operation_spinner.adapter = CustomArrayAdapter(context,
                TransactionType.values().toList()) { transactionType -> getString(transactionType.resId) }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()

    }


    companion object {

        @JvmStatic
        fun newInstance() =
                AddCategoryFragment()
    }
}
