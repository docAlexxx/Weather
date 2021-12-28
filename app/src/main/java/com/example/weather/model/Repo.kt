package com.example.weather.model

interface Repo {
    fun getWeatherFromServer(): WeatherData
    fun getWeatherFromLocalStorage(): WeatherData
}