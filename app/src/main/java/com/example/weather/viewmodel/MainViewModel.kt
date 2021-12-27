package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppStatement> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData(): LiveData<AppStatement> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppStatement.Loading(0))
            sleep(3000)
            liveData.postValue(AppStatement.Loading(100))
            sleep(3000)
            liveData.postValue(AppStatement.Success(-15, -20))
        }.start()
    }
}