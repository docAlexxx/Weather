package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppStatement> = MutableLiveData(),
    private val repoImpl: RepoImpl = RepoImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppStatement> {
        return liveData
    }

    fun getWeatherFromLocalSourceRus() = getWeather(true)

    fun getWeatherFromLocalSourceWorld() = getWeather(false)

    //  fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(true)

    fun getWeather(isRussian: Boolean) {
        liveData.postValue(AppStatement.Loading(0))
        Thread {
            sleep(1000)
            val randomResult = (1..100).random()
            if (randomResult > 90) {
                liveData.postValue(AppStatement.Error(IllegalStateException("")))
            } else {
                liveData.postValue(
                    AppStatement.Success(
                        if (isRussian) {
                            repoImpl.getWeatherFromLocalStorageRus()
                        } else {
                            repoImpl.getWeatherFromLocalStorageWorld()
                        }
                    )
                )
            }
        }.start()
    }
}