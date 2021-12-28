package com.example.weather.model

data class WeatherData(
    val city: City = getDefaultCity(),
    val temperature: Int = -20,
    val feelsLike: Int = -25
)

data class City(val name: String, val lon: Double, val lat: Double)

fun getDefaultCity() = City("Samara", 53.2001, 50.15)
