package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherData(
    val city: City = getDefaultCity(),
    val temperature: Int = -20,
    val feelsLike: Int = -25
) : Parcelable

@Parcelize
data class City(val name: String, val lat: Double, val lon: Double) : Parcelable

fun getDefaultCity() = City("Samara", 53.12, 50.06)

fun getWorldCities(): List<WeatherData> {
    return listOf(
        WeatherData(City("London", 51.5085300, -0.1257400), 1, 2),
        WeatherData(City("Tokyo", 35.6895000, 139.6917100), 3, 4),
        WeatherData(City("Paris", 48.8534100, 2.3488000), 5, 6),
        WeatherData(City("Berlin", 52.52000659999999, 13.404953999999975), 7, 8),
        WeatherData(City("Rome", 41.9027835, 12.496365500000024), 9, 10),
        WeatherData(City("Minsk", 53.90453979999999, 27.561524400000053), 11, 12),
        WeatherData(City("Istanbul", 41.0082376, 28.97835889999999), 13, 14),
        WeatherData(City("Washington", 38.9071923, -77.03687070000001), 15, 16),
        WeatherData(City("Kiev", 50.4501, 30.523400000000038), 17, 18),
        WeatherData(City("Beijing", 39.90419989999999, 116.40739630000007), 19, 20)
    )
}

fun getRussianCities(): List<WeatherData> {
    return listOf(
        WeatherData(City("Самара", 53.12, 50.06), 10, 15),
        WeatherData(City("Москва", 55.755826, 37.617299900000035), 1, 2),
        WeatherData(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
        WeatherData(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
        WeatherData(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8),
        WeatherData(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
        WeatherData(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
        WeatherData(City("Челябинск", 55.1644419, 61.4368432), 13, 14),
        WeatherData(City("Омск", 54.9884804, 73.32423610000001), 15, 16),
        WeatherData(City("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
        WeatherData(City("Уфа", 54.7387621, 55.972055400000045), 19, 20)
    )
}
