package com.example.weather.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.lesson10.MapsFragment
import com.example.weather.lesson9.PhonelistFragment
import com.example.weather.view.history.HistoryFragment
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
//        val listWeather = App.getHistoryWeatherDao().getAllHistoryWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_history -> {
                val fragmentHistory = supportFragmentManager.findFragmentByTag("tagH")
                if (fragmentHistory == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, HistoryFragment.newInstance(), "tagH")
                        .addToBackStack("")
                        .commit()
                }
                true
            }
            R.id.menu_phonelist -> {
                val fragmentList = supportFragmentManager.findFragmentByTag("tagL")
                if (fragmentList == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, PhonelistFragment.newInstance(), "tagL")
                        .addToBackStack("").commit()
                }
                true
            }
            R.id.menu_google_maps -> {
                val fragmentMap = supportFragmentManager.findFragmentByTag("tagM")
                if (fragmentMap == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, MapsFragment(), "tagM").addToBackStack("")
                        .commit()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

}