package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.model.WeatherData

const val BUNDLE_KEY = "key"

class CityFragment : Fragment() {
    var _binding: FragmentCityBinding? = null

    private val binding: FragmentCityBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(bundle: Bundle): CityFragment {
            val fragment = CityFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<WeatherData>(BUNDLE_KEY)
        if (weather != null) {
            setWeatherData(weather)
        }
    }

    private fun setWeatherData(weather: WeatherData) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text =
            "${weather.city.lat}, ${weather.city.lon}"
        binding.temperatureValue.text = "${weather.temperature}"
        binding.feelsLikeValue.text = "${weather.feelsLike}"
    }

}