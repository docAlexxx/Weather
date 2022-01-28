package com.example.weather.repo

import com.example.weather.model.City
import com.example.weather.model.WeatherData
import com.example.weather.room.App
import com.example.weather.room.HistoryWeatherEntity

class RepoHistoryWeatherImpl: RepoHistoryWeather {
    override fun getAllHistoryWeather(): List<WeatherData> {
        return convertHistoryWeatherEntityToWeatherData(
            App.getHistoryWeatherDao().getAllHistoryWeather()
        )
    }

    override fun saveWeather(weather: WeatherData) {
        App.getHistoryWeatherDao().insert(
            convertWeatherDataToHistoryWeatherEntity(weather)
        )
    }

    private fun convertHistoryWeatherEntityToWeatherData(entityList: List<HistoryWeatherEntity>): List<WeatherData> {

        return entityList.map {
            WeatherData(
                City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon
            )
        }

    }

    private fun convertWeatherDataToHistoryWeatherEntity(weather: WeatherData) =
        HistoryWeatherEntity(
            0,
            weather.city.name,
            weather.temperature,
            weather.feelsLike,
            weather.icon
        )

}