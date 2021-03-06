package ru.daryasoft.fintracker.initialization

import android.app.Application
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import ru.daryasoft.fintracker.category.data.CategoryDao
import ru.daryasoft.fintracker.common.AppDatabase
import ru.daryasoft.fintracker.rate.RateNetworkDataSource
import ru.daryasoft.fintracker.rate.ResponseToRateConverter
import ru.daryasoft.fintracker.transaction.data.TransactionDao
import java.lang.reflect.Type
import javax.inject.Singleton

/**
 * Dagger-модуль с поставщиками зависимостей.
 */
@Module
class MainProvideModule {

    @Provides
    @Singleton
    fun provideSharedPreference(context: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideRateNetworkDataSource(): RateNetworkDataSource {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.cbr-xml-daily.ru/")
                .addConverterFactory(object : Converter.Factory() {
                    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
                        return ResponseToRateConverter()
                    }
                })
                .build()
        return retrofit.create(RateNetworkDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Application): AppDatabase {
        return AppDatabase.getInstance(context)
    }


    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return  appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return  appDatabase.transactionDao()
    }

}