package com.example.weather.viewmodel

import com.example.weather.model.WeatherDTO

sealed class CityLoadStatement {
    data class Loading(var progress: Int) : CityLoadStatement()
    data class Success(val weatherData: WeatherDTO) : CityLoadStatement()
    data class Error(var error: String) : CityLoadStatement()
}