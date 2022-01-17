package com.example.weather.Utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.example.weather.model.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader (private val onWeatherLoaded: OnWeatherLoaded) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(lat: Double, lon: Double) {

        Thread {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 2000
                addRequestProperty("X-Yandex-API-Key", "c03274e4-2ca8-4633-b2dc-98865a65c693")
            }
            val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
            val weatherDTO: WeatherDTO? =
                Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)
            Handler(Looper.getMainLooper()).post {
                onWeatherLoaded.onLoaded(weatherDTO)
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}

interface OnWeatherLoaded {
    fun onLoaded(weatherDTO: WeatherDTO?)
    fun onFailed()
}