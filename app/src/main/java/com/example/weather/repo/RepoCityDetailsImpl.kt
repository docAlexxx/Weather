package com.example.weather.repo

import com.example.weather.BuildConfig
import com.example.weather.Utils.API_KEY
import com.example.weather.Utils.YANDEX_API_URL
import com.example.weather.model.WeatherDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoCityDetailsImpl : RepoCityDetails {

    override fun getWeatherFromServer(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {

        val retrofit = Retrofit.Builder()
            .baseUrl(YANDEX_API_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build().create(WeatherApi::class.java)
        retrofit.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
}