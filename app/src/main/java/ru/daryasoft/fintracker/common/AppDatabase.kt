package ru.daryasoft.fintracker.common

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import kotlinx.coroutines.experimental.launch
import ru.daryasoft.fintracker.account.data.AccountDao
import ru.daryasoft.fintracker.category.data.CategoryDao
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.transaction.data.TransactionDao
import java.math.BigDecimal


@Database(entities = [TransactionDB::class, Account::class, Category::class], version = 1)
@TypeConverters(BDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DB_NAME = "users.db"

         fun createPersistentDatabase(context: Context): AppDatabase {
             val database = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
             //ВНИМАНИЕ!!! Никогда так не делай, для инициализации бд нужно реализовать callback в билдере выше
             launch {
                 database.categoryDao().insert(Category("Зарплата", TransactionType.INCOME, "ic_salary", 1))
                 database.categoryDao().insert(Category("Сбережения", TransactionType.INCOME, "ic_saving", 2))
                 database.categoryDao().insert(Category("Транспорт", TransactionType.OUTCOME, "ic_transport", 3))
                 database.categoryDao().insert(Category("Еда", TransactionType.OUTCOME, "ic_food", 4))
                 database.accountDao().insert(Account("Наличные руб", Money(BigDecimal.valueOf(600.00), Currency.RUB), 1))
                 database.accountDao().insert(Account("Карта руб", Money(BigDecimal.valueOf(1600.00), Currency.RUB), 2))
                 database.accountDao().insert(Account("Карта доллары", Money(BigDecimal.valueOf(20600.00), Currency.USD), 3))
//                 database.transactionDao().insertTransaction(TransactionDB(Account(), Money(BigDecimal.valueOf(600), Currency.RUB), Date(), Category(), Periodicity.Without, false, null, 1, 1))
             }
             return database
         }
    }
}