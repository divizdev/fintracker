package ru.daryasoft.fintracker.rate

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import ru.daryasoft.fintracker.entity.Rate

@Dao
interface RateDao {

    @Insert
    fun insertRates(list: List<Rate>)


}