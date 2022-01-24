package com.example.weather.repo

import com.example.weather.model.getRussianCities
import com.example.weather.model.getWorldCities

class RepoCitiesListImpl : RepoCitiesList {

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}