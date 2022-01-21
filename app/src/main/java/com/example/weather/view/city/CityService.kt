package com.example.weather.view.city

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.weather.BuildConfig
import com.example.weather.Utils.API_KEY
import com.example.weather.Utils.BUNDLE_KEY_WEATHER
import com.example.weather.model.WeatherDTO
import com.example.weather.view.MainActivity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.util.stream.Collectors


class CityService(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        loadWeather(lat, lon)
    }

    fun loadWeather(lat: Double, lon: Double) {

            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
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
                val intent = Intent().apply {
                    putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
                }

                Handler(Looper.getMainLooper()).post {
                    startActivity(Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
                    })
                }

    }

    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}