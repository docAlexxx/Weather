package com.example.weather.repo

import com.example.weather.model.WeatherDTO

interface RepoCityDetails {
    fun getWeatherFromServer(lat: Double,lon: Double,callback: retrofit2.Callback<WeatherDTO>)
}