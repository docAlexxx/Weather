package com.example.weather.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.room.App
import com.example.weather.view.list.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance()).commit()
        }
        val listWeather = App.getHistoryWeatherDao().getAllHistoryWeather()
    }


}