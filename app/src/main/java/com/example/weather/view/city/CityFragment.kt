package com.example.weather.view.city

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.Utils.*
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.model.WeatherDTO
import com.example.weather.model.WeatherData
import com.google.android.material.snackbar.Snackbar


class CityFragment : Fragment(), WeatherLoader.OnWeatherLoaded {
    private var _binding: FragmentCityBinding? = null

    private val binding: FragmentCityBinding
        get() {
            return _binding!!
        }

    //  private val weatherLoader = WeatherLoader(this)
    lateinit var localWeather: WeatherData

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireActivity())

            .unregisterReceiver(receiver)
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val weatherDTO = it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)
                if (weatherDTO != null) {
                    setWeatherData(weatherDTO)
                } else {

                }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(bundle: Bundle) = CityFragment().also { it.arguments = bundle }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getParcelable<WeatherData>(BUNDLE_KEY)?.let {
                localWeather = it
                requireActivity().startService(
                    Intent(
                        requireActivity(),
                        CityService::class.java
                    ).apply {
                        putExtra(LATITUDE_CITY, it.city.lat)
                        putExtra(LONGITUDE_CITY, it.city.lon)
                    })
                LocalBroadcastManager.getInstance(requireActivity())
                    .registerReceiver(receiver, IntentFilter(DETAILS_INTENT_FILTER))
            }
        }
    }

    private fun setWeatherData(weatherDTO: WeatherDTO) {
        with(binding) {
            localWeather.run {
                cityName.text = city.name
                cityCoordinates.text =
                    "${weatherDTO.info.lat}, ${weatherDTO.info.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
        }

    }

    override fun onLoaded(weatherDTO: WeatherDTO?) {
        weatherDTO?.let {
            setWeatherData(weatherDTO)
        }
    }

    override fun onFailed() {
        Snackbar.make(
            binding.root,
            getString(R.string.error_load_text) + " ${localWeather.city.name}",
            Snackbar.LENGTH_LONG
        ).show()
    }


}