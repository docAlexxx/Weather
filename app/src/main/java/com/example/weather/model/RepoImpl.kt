package com.example.weather.model

class RepoImpl :Repo {
    override fun getWeatherFromServer(): WeatherData {
        return WeatherData()
    }

    override fun getWeatherFromLocalStorage(): WeatherData {
        return WeatherData()
    }

}