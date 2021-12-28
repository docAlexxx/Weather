package com.example.weather.viewmodel

import com.example.weather.model.WeatherData

sealed class AppStatement {
    data class Loading(var progress: Int) : AppStatement()
    data class Success(val weatherData: WeatherData) : AppStatement()
    data class Error(var error: Throwable) : AppStatement()
}