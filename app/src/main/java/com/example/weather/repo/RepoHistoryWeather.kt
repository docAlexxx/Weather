package com.example.weather.repo

import com.example.weather.model.WeatherData

interface RepoHistoryWeather {
    fun getAllHistoryWeather():List<WeatherData>
    fun saveWeather(weather: WeatherData)
}