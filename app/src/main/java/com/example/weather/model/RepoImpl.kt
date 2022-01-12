package com.example.weather.model

class RepoImpl : Repo {
    override fun getWeatherFromServer() = WeatherData()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}