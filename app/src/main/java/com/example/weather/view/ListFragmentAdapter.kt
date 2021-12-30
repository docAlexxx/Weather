package com.example.weather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.model.WeatherData

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData: List<WeatherData> = listOf()

    fun setWeather(data: List<WeatherData>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(        parent: ViewGroup,        viewType: Int
    ): MainFragmentAdapter.MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent,false)
    }

    override fun onBindViewHolder(holder: MainFragmentAdapter.MainViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: WeatherData) {
          itemView.findViewById<TextView>(R.id.recyclerItemTextView).text=weather.city.name
            itemView.setOnClickListener{

            }
        }
    }
}