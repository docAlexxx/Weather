package com.example.weather.view.city

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.BuildConfig
import com.example.weather.Utils.*
import com.example.weather.model.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class CityService(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_CITY, 0.0)
            val lon = it.getDoubleExtra(LONGITUDE_CITY, 0.0)
            loadWeather(lat, lon)
        }
    }

    private fun loadWeather(lat: Double, lon: Double) {

        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
        Thread {
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection)
            httpsURLConnection.apply {
                requestMethod = "GET"
                readTimeout = 2000
                addRequestProperty(API_KEY, BuildConfig.WEATHER_API_KEY)
            }

            val bufferedReader =
                BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
            val weatherDTO: WeatherDTO? =
                Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)
            val intent = Intent(DETAILS_INTENT_FILTER).apply {
                putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
            }

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            httpsURLConnection.disconnect()
        }.start()

    }

    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}