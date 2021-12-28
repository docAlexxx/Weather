package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.viewmodel.AppStatement
import com.example.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


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
        fun newInstance() = CityFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<AppStatement> { checkData(it) })
        viewModel.getWeather()
    }

    fun checkData(appState: AppStatement) {
        when (appState) {
            is AppStatement.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainView, "Can't load data", Snackbar.LENGTH_LONG)
                    .setAction("Try again") {
                        viewModel.getWeather()
                    }.show()
            }
            is AppStatement.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppStatement.Success -> {
                binding.loadingLayout.visibility = View.GONE
                //  Snackbar.make(binding.mainView,"${appState.temperature} (${appState.feelsLike} )",Snackbar.LENGTH_LONG).show()

                binding.cityName.text = appState.weatherData.city.name
                binding.cityCoordinates.text =
                    "${appState.weatherData.city.lat}, ${appState.weatherData.city.lon}"
                binding.temperatureValue.text = "${appState.weatherData.temperature}"
                binding.feelsLikeValue.text = "${appState.weatherData.feelsLike}"

                Snackbar.make(
                    binding.mainView,
                    "Success",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }


    }
}