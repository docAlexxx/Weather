package com.example.weather.repo

import com.example.weather.BuildConfig
import com.example.weather.Utils.API_KEY
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class RepoCityDetailsImpl : RepoCityDetails {

    override fun getWeatherFromServer(url: String, callback: Callback) {
        val builder= Request.Builder().apply {
            header(API_KEY, BuildConfig.WEATHER_API_KEY)
            url(url)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }
}