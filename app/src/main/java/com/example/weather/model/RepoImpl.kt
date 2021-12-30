package com.example.weather.model

class RepoImpl : Repo {
    override fun getWeatherFromServer(): WeatherData {
        return WeatherData()
    }

    override fun getWeatherFromLocalStorageRus(): List<WeatherData> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<WeatherData> {
        return getWorldCities()
    }


}