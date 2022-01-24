package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.Utils.YANDEX_API_URL
import com.example.weather.Utils.YANDEX_API_URL_END_POINT
import com.example.weather.model.WeatherDTO
import com.example.weather.repo.RepoCityDetailsImpl
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class DetailsViewModel(
    private val liveData: MutableLiveData<CityLoadStatement> = MutableLiveData(),
) : ViewModel() {

    private val repoImpl: RepoCityDetailsImpl by lazy {
        RepoCityDetailsImpl()
    }

    fun getLiveData() = liveData

    fun getWeatherFromRemoteServer(lat: Double, lon: Double) {
        liveData.postValue(CityLoadStatement.Loading(0))
        repoImpl.getWeatherFromServer(
            YANDEX_API_URL + YANDEX_API_URL_END_POINT + "?lat=${lat}&lon=${lon}",
            callback
        )
    }

    private val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                response.body()?.let {
                    val json = it.string()
                    liveData.postValue(
                        CityLoadStatement.Success(
                            Gson().fromJson(
                                json,
                                WeatherDTO::class.java
                            )
                        )
                    )
                }
            } else {
                // TODO HW
            }
        }
    }
}
