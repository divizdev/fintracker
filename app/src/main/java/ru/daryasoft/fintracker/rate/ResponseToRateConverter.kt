package ru.daryasoft.fintracker.rate

import android.util.Log
import com.github.mikephil.charting.charts.Chart.LOG_TAG
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import ru.daryasoft.fintracker.common.Constants
import ru.daryasoft.fintracker.entity.Currency
import ru.daryasoft.fintracker.entity.Rate

private const val CANT_CONVERT_ERROR_MESSAGE = "Can't convert service response into list of rate entities."

/**
 * Конвертер для получения списка сущностей с информацией о курсах валют из ответа сервиса.
 */
class ResponseToRateConverter : Converter<ResponseBody, List<Rate>> {

    override fun convert(responseBody: ResponseBody): List<Rate> {
        val rateList = mutableListOf<Rate>()
        try {
            val items = JSONObject(responseBody.string()).getJSONObject("Valute")
            for (currency in Currency.values()) {
                if (currency != Constants.DEFAULT_CURRENCY) {
                    val item = items.getJSONObject(currency.name)
                    rateList.add(Rate(currency, item.getDouble("Value")))
                }
            }
        } catch (e: JSONException) {
            Log.e(LOG_TAG, CANT_CONVERT_ERROR_MESSAGE, e)
        }

        return rateList
    }
}
