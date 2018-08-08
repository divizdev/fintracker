package ru.daryasoft.fintracker.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.daryasoft.fintracker.R

/**
 * Адаптер для выпадающего списка, позволяющий передавать в список элементы с функцией,
 * заменяющей toString.
 */
class CustomArrayAdapter<T>(context: Context?, data: List<T>, private val toStringFun: (element: T) -> String)
    : ArrayAdapter<T>(context, R.layout.item_spinner, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner, null)
        }
        view?.findViewById<TextView>(R.id.Item_text)?.text = toStringFun.invoke(getItem(position))
        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner, null)
        }
        view?.findViewById<TextView>(R.id.Item_text)?.text = toStringFun.invoke(getItem(position))
        return view!!
    }
}