package com.example.weather.repo

import com.example.weather.model.WeatherData
import okhttp3.Callback

interface RepoCityDetails {
    fun getWeatherFromServer(url:String,callback: Callback)
}