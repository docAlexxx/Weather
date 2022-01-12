package com.example.weather.view.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.model.WeatherData

const val BUNDLE_KEY = "key"

class CityFragment : Fragment() {
    private var _binding: FragmentCityBinding? = null

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
            it.getParcelable<WeatherData>(BUNDLE_KEY)?.run {
                setWeatherData(this)
            }
        }
    }

    private fun setWeatherData(weather: WeatherData) {
        with(binding) {
            weather.run {
                cityName.text = city.name
                cityCoordinates.text =
                    "${city.lat}, ${city.lon}"
                temperatureValue.text = "$temperature"
                feelsLikeValue.text = "$feelsLike"
            }
        }
    }

}