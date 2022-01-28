package com.example.weather.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.databinding.HistoryRecyclerItemBinding
import com.example.weather.model.WeatherData
import com.example.weather.view.list.OnItemClick

class HistoryFragmentAdapter (val listener: OnItemClick) :
    RecyclerView.Adapter<HistoryFragmentAdapter.HistoryCityViewHolder>() {

    private var weatherData: List<WeatherData> = listOf()


    fun setWeather(data: List<WeatherData>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryFragmentAdapter.HistoryCityViewHolder {
        val binding: HistoryRecyclerItemBinding =
            HistoryRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HistoryCityViewHolder(binding.root)
    }
    override fun onBindViewHolder(holder: HistoryFragmentAdapter.HistoryCityViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }
    override fun getItemCount(): Int {
        return weatherData.size
    }
    inner class HistoryCityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: WeatherData) {
            with(HistoryRecyclerItemBinding.bind(itemView)){
                cityName.text = weather.city.name
                temperature.text = "${weather.temperature}"
                feelsLike.text = "${weather.feelsLike}"
                icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                root.setOnClickListener {
                    listener.onItemClick(weather)
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
}