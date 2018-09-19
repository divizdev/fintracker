package ru.daryasoft.fintracker.common

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import ru.daryasoft.fintracker.account.data.AccountDao
import ru.daryasoft.fintracker.category.data.CategoryDao
import ru.daryasoft.fintracker.entity.*
import ru.daryasoft.fintracker.transaction.data.TransactionDao
import java.math.BigDecimal
import java.util.concurrent.Executors


@Database(entities = [TransactionDB::class, Account::class, Category::class], version = 1)
@TypeConverters(BDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DB_NAME = "users.db"


        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DB_NAME)
                        // prepopulate the database after onCreate was called
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // insert the data on the IO Thread
                                ioThread {
                                    val instance = getInstance(context)
                                    instance.categoryDao().insert(Category("Зарплата", TransactionType.INCOME, 1))
                                    instance.categoryDao().insert(Category("Сбережения", TransactionType.INCOME, 2))
                                    instance.categoryDao().insert(Category("Транспорт", TransactionType.OUTCOME, 3))
                                    instance.categoryDao().insert(Category("Еда", TransactionType.OUTCOME, 4))
                                    instance.accountDao().insert(Account("Наличные", Money(BigDecimal.valueOf(600.00), Currency.RUB), 1))
                                    instance.accountDao().insert(Account("Карта ВТБ", Money(BigDecimal.valueOf(1600.00), Currency.RUB), 2))
                                    instance.accountDao().insert(Account("Карта Raiffeisen", Money(BigDecimal.valueOf(20600.00), Currency.USD), 3))
                                }
                            }
                        })
                        .build()


//         fun createPersistentDatabase(context: Context): AppDatabase {
//             val database = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).addCallback().build()
//             //ВНИМАНИЕ!!! Никогда так не делай, для инициализации бд нужно реализовать callback в билдере выше
//             launch {
//                 database.categoryDao().insert(Category("Зарплата", TransactionType.INCOME, 1))
//                 database.categoryDao().insert(Category("Сбережения", TransactionType.INCOME, 2))
//                 database.categoryDao().insert(Category("Транспорт", TransactionType.OUTCOME, 3))
//                 database.categoryDao().insert(Category("Еда", TransactionType.OUTCOME, 4))
//                 database.accountDao().insert(Account("Наличные руб", Money(BigDecimal.valueOf(600.00), Currency.RUB), 1))
//                 database.accountDao().insert(Account("Карта руб", Money(BigDecimal.valueOf(1600.00), Currency.RUB), 2))
//                 database.accountDao().insert(Account("Карта доллары", Money(BigDecimal.valueOf(20600.00), Currency.USD), 3))
////                 database.transactionDao().insertTransaction(TransactionDB(Account(), Money(BigDecimal.valueOf(600), Currency.RUB), Date(), Category(), Periodicity.Without, false, null, 1, 1))
//             }
//             return database
//         }
    }
}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}


