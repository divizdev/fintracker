package ru.daryasoft.fintracker.transaction.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class TransactionItemTouchHelper(dragDirs: Int, swipeDirs: Int,  private val onDeleteAction: (position: Int) -> Unit) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onDeleteAction.invoke(viewHolder.adapterPosition)
    }


}