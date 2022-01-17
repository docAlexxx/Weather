package com.example.weather.Utils

import com.example.weather.model.WeatherDTO

class WeatherLoader (private val onWeatherLoaded: OnWeatherLoaded) {


}

interface OnWeatherLoaded {
    fun onLoaded(weatherDTO: WeatherDTO?)
    fun onFailed()
}