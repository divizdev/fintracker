package ru.daryasoft.fintracker.entity

import ru.daryasoft.fintracker.R

enum class Periodicity(val resId: Int, val code: Int) {
    Without(R.string.Without, 0),
    OnceMonth(R.string.OnceMonth, 1)

}