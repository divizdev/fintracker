package ru.daryasoft.fintracker.category.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.category_item.view.*
import ru.daryasoft.fintracker.R
import ru.daryasoft.fintracker.entity.Category

class CategoryListAdapter(private var list: List<Category>,
                          private val onDeleteAction: (position: Int) -> Unit) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = list[position]
        holder.setData(transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(newList: List<Category>) {
        list = newList
        notifyDataSetChanged() //Добавить DiffUtil
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnLongClickListener {
                onDeleteAction.invoke(adapterPosition)
                true
            }
        }

        fun setData(category: Category) {

            itemView.name_category_text_view.text = category.name
            itemView.name_type_operation.text = itemView.resources.getText(category.transactionType.resId)
        }


    }
}