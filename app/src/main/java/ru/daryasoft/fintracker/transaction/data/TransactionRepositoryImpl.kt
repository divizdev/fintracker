package ru.daryasoft.fintracker.transaction.data

/**
 * Репозиторий для работы с финансовыми транзакциями.
 */
//@Singleton
//class TransactionRepositoryImpl @Inject constructor(accountRepository: AccountRepository,
//                                                    categoryRepository: CategoryRepository, db: AppDatabase) : TransactionRepository {
//
//    private val transactions = MutableLiveData<List<TransactionDB>>()
//    private val accounts = accountRepository.getAll()
//    private val categories = categoryRepository.getAll()
//
//    init {
//        if (accounts.value != null && categories.value != null) {
//            val accountsForGet = accounts.value as List<Account>
//            val categoriesForGet = categories.value as List<Category>
//            transactions.value = listOf(
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(10000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[0]),
//                    TransactionDB(accountsForGet[1], Money(BigDecimal.valueOf(1000.00), accountsForGet[1].money.currency), Date(), categoriesForGet[1]),
//                    TransactionDB(accountsForGet[2], Money(BigDecimal.valueOf(1000.00), accountsForGet[2].money.currency), Date(), categoriesForGet[2]),
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(1000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[3]),
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(2000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[3]),
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(3000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[3]),
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(1000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[2]),
//                    TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(200.00), accountsForGet[0].money.currency), Date(), categoriesForGet[2]),
//                    TransactionDB(accountsForGet[1], Money(BigDecimal.valueOf(1000.00), accountsForGet[1].money.currency), Date(), categoriesForGet[0]))
//        val transactionDao = db.transactionDao()
//        launch {
//            transactionDao.insertTransaction(TransactionDB(accountsForGet[0], Money(BigDecimal.valueOf(10000.00), accountsForGet[0].money.currency), Date(), categoriesForGet[0])
//                    )
//        }
//        }
//    }
//
//    override fun getAll(): LiveData<List<TransactionDB>> {
//
//        return transactions
//    }
//
//    override fun query(category: Category, account: Account?): LiveData<List<TransactionDB>> {
//        val filteredValues = MutableLiveData<List<TransactionDB>>()
//        filteredValues.value = transactions.value?.filter { it.category == category && (account == null || it.account == account) } ?: listOf()
//        return filteredValues
//    }
//
//    override fun add(transactionDB: TransactionDB, account: Account) {
//        val mutableList = transactions.value?.toMutableList()
//        mutableList?.add(transactionDB)
//        transactions.value = mutableList
//
//        transactionDB.account.money.value = transactionDB.account.money.value.add( if (transactionDB.category.transactionType == TransactionType.INCOME)
//           transactionDB.sum.value else -transactionDB.sum.value)
//    }
//
//    override fun delete(transactionDB: TransactionDB) {
//        val mutableList = transactions.value?.toMutableList()
//        mutableList?.remove(transactionDB)
//        transactions.value = mutableList
//    }
//}