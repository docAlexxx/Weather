package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.WeatherDTO
import com.example.weather.model.WeatherData
import com.example.weather.repo.RepoCityDetailsImpl
import com.example.weather.repo.RepoHistoryWeatherImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    private val liveData: MutableLiveData<CityLoadStatement> = MutableLiveData(),
) : ViewModel() {

    private val repoHistoryWeatherImpl: RepoHistoryWeatherImpl = RepoHistoryWeatherImpl()
    private val repoImpl: RepoCityDetailsImpl by lazy {
        RepoCityDetailsImpl()
    }

    fun getLiveData() = liveData

    fun saveWeather(weather: WeatherData) {
        Thread {
            repoHistoryWeatherImpl.saveWeather(weather)
        }.start()
    }

    fun getWeatherFromRemoteServer(lat: Double, lon: Double) {
        liveData.postValue(CityLoadStatement.Loading(0))
        repoImpl.getWeatherFromServer(lat, lon, callback)
    }

    private val callback = object : Callback<WeatherDTO> {

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(CityLoadStatement.Success(it))
                }
            } else {
                var errorText = response.message() + "( ${response.code()} )"

                liveData.postValue(
                    CityLoadStatement.Error(
                        identifyError(
                            response.code(),
                            errorText
                        )
                    )
                )
            }
        }
    }

    fun identifyError(errorCode: Int, errorText: String): String {
        return when (errorCode) {
            in 300..399 -> "Redirection! "
            in 400..499 -> "Client Error! "
            in 500..599 -> "Server Error! "
            else -> "Informational! "
        }
    }
}
