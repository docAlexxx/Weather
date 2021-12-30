package com.example.weather.model

interface Repo {
    fun getWeatherFromServer(): WeatherData
    fun getWeatherFromLocalStorageRus(): List<WeatherData>
    fun getWeatherFromLocalStorageWorld(): List<WeatherData>
}