package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.repo.RepoHistoryWeatherImpl

class HistoryViewModel(
    private val liveData: MutableLiveData<AppStatement> = MutableLiveData(),
) : ViewModel() {

    private val repoHistoryWeatherImpl: RepoHistoryWeatherImpl by lazy {
        RepoHistoryWeatherImpl()
    }

    fun getLiveData() = liveData

    fun getAllHistory() {
        Thread {
            val listWeather = repoHistoryWeatherImpl.getAllHistoryWeather()
            liveData.postValue(AppStatement.Success(listWeather))
        }.start()

    }


}