package ru.daryasoft.fintracker.category.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ru.daryasoft.fintracker.entity.Category
import ru.daryasoft.fintracker.entity.TransactionType

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT * FROM Category Where idKeyCategory = :id")
    fun getById(id: Long): Category

    @Query("SELECT * FROM category Where transactionType = :transactionType")
    fun getByTransactionType(transactionType: TransactionType):LiveData<List<Category>>

    @Delete
    fun delete(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)
}