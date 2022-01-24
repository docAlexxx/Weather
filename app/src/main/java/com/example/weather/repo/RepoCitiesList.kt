package com.example.weather.repo

import com.example.weather.model.WeatherData

interface RepoCitiesList {
    fun getWeatherFromLocalStorageRus(): List<WeatherData>
    fun getWeatherFromLocalStorageWorld(): List<WeatherData>
}