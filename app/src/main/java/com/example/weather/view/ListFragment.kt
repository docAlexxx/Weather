package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentListBinding
import com.example.weather.model.WeatherData
import com.example.weather.viewmodel.AppStatement
import com.example.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment(),OnItemClick{

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() {
            return _binding!!
        }

    private val adapter = ListFragmentAdapter()
    private var isRussian = true

    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppStatement> { checkData(it) })
        initView()
    }

    private fun initView() {
        with(binding) {
            listFragmentRecyclerView.adapter = adapter
            listFragmentFAB.setOnClickListener {
                changeRegion()
            }
        }
    }

    private fun changeRegion() {
        isRussian = !isRussian
        with(binding) {
            if (isRussian) {
                viewModel.getWeatherFromLocalSourceRus()
                listFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                viewModel.getWeatherFromLocalSourceWorld()
                listFragmentFAB.setImageResource(R.drawable.ic_earth)
            }
        }

    }

    fun checkData(appState: AppStatement) {
        when (appState) {
            is AppStatement.Error -> {
                binding.listFragmentLoadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Can't load data", Snackbar.LENGTH_LONG)
                    .setAction("Try again") {
                        changeRegion()
                    }.show()
            }
            is AppStatement.Loading -> {
                binding.listFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppStatement.Success -> {
                binding.listFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)

                Snackbar.make(
                    binding.root,
                    "Success",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }


    }

    override fun onItemClick(weather: WeatherData) {
        val bundle=Bundle()
        bundle.putParcelable(BUNDLE_KEY,weather)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,CityFragment.newInstance(bundle))
            .addToBackStack("").commit()
    }
}