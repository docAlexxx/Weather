package com.example.weather.view.list

import com.example.weather.model.WeatherData

interface OnItemClick {
    fun onItemClick(weather: WeatherData)
}