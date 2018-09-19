package ru.daryasoft.fintracker.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Курс валюты (по отношению к валюте по умолчанию).
 */
@Entity
data class Rate(
        @PrimaryKey
        val currency: Currency,
        val ratio: Double
)