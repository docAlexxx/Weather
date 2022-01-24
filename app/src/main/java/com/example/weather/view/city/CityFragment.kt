package com.example.weather.view.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.Utils.BUNDLE_KEY
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.model.WeatherDTO
import com.example.weather.model.WeatherData
import com.example.weather.viewmodel.CityLoadStatement
import com.example.weather.viewmodel.DetailsViewModel


class CityFragment : Fragment() {
    private var _binding: FragmentCityBinding? = null

    private val binding: FragmentCityBinding
        get() {
            return _binding!!
        }

    private lateinit var localWeather: WeatherData

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private fun renderData(cityLoadStatement: CityLoadStatement) {
        with(binding) {
            when (cityLoadStatement) {
                is CityLoadStatement.Error -> {
                    // HW
                }
                is CityLoadStatement.Loading -> {
                    // HW
                }
                is CityLoadStatement.Success -> {
                    val weather = cityLoadStatement.weatherData
                    setWeatherData(weather)
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
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        arguments?.let {
            it.getParcelable<WeatherData>(BUNDLE_KEY)?.let {
                localWeather = it
                viewModel.getWeatherFromRemoteServer(localWeather.city.lat, localWeather.city.lon)
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

}