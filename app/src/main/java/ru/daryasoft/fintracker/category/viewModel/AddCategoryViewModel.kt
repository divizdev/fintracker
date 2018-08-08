package ru.daryasoft.fintracker.category.viewModel

import android.arch.lifecycle.ViewModel
import ru.daryasoft.fintracker.category.data.CategoryRepository
import ru.daryasoft.fintracker.entity.Category
import javax.inject.Inject

class AddCategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {

    fun onAddCategory(category: Category) {
        categoryRepository.addCategory(category)
    }

}