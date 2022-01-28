package com.example.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val city: String,
    val temperature: Int,
    val feelsLike: Int,
    val icon: String
)