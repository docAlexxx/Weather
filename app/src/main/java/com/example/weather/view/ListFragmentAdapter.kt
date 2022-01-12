package com.example.weather.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.RecyclerItemBinding
import com.example.weather.model.WeatherData

class ListFragmentAdapter(val listener: OnItemClick) :
    RecyclerView.Adapter<ListFragmentAdapter.MainViewHolder>() {

    private var weatherData: List<WeatherData> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(data: List<WeatherData>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MainViewHolder {
      //  return MainViewHolder(
      //      LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
      //  )
        val binding: RecyclerItemBinding =
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MainViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: WeatherData) {
         //   itemView.findViewById<TextView>(R.id.recyclerItemTextView).text = weather.city.name
         //   itemView.setOnClickListener {
                val binding = RecyclerItemBinding.bind(itemView)
                binding.recyclerItemTextView.text = weather.city.name
                binding.root.setOnClickListener {
                listener.onItemClick(weather)
            }

        }
    }
}