package com.example.weather.view.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.Utils.BUNDLE_KEY
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.model.WeatherDTO
import com.example.weather.model.WeatherData
import com.example.weather.viewmodel.CityLoadStatement
import com.example.weather.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*


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
                    loadingLayout.visibility = View.GONE
                    Snackbar.make(binding.mainView, cityLoadStatement.error, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry_text) {
                            viewModel.getWeatherFromRemoteServer(
                                localWeather.city.lat,
                                localWeather.city.lon
                            )
                        }.show()
                }

                is CityLoadStatement.Loading -> {
                    loadingLayout.visibility = View.VISIBLE
                }
                is CityLoadStatement.Success -> {
                    loadingLayout.visibility = View.GONE
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
                //         cityCoordinates.text =
                //              "${weatherDTO.info.lat}, ${weatherDTO.info.lon}"
                minTempValue.text = "${weatherDTO.forecast.parts[0].tempMin}"
                maxTempValue.text = "${weatherDTO.forecast.parts[0].tempMax}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
                windDirection.text = "${weatherDTO.fact.windDir}"
                windSpeed.text = "${weatherDTO.fact.windSpeed}"


//                Glide.with(headerIcon.context)
//                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
//                    .into(headerIcon)

//                Picasso.get()
//                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
//                    .into(headerIcon)

                headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                weatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weatherDTO.fact.icon}.svg")
            }
        }
    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

}