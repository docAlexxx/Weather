package com.example.weather.view

import com.example.weather.model.WeatherData

interface OnItemClick {
    fun onItemClick(weather: WeatherData)
}