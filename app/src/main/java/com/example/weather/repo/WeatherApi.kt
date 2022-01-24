package com.example.weather.repo

import com.example.weather.Utils.API_KEY
import com.example.weather.Utils.YANDEX_API_URL_END_POINT
import com.example.weather.model.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    @GET(YANDEX_API_URL_END_POINT)
    fun getWeather(
        @Header(API_KEY) apikey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}