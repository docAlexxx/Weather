package com.example.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentHistoryBinding
import com.example.weather.model.WeatherData
import com.example.weather.view.list.OnItemClick
import com.example.weather.viewmodel.AppStatement
import com.example.weather.viewmodel.HistoryViewModel
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment(), OnItemClick {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() {
            return _binding!!
        }

    private val adapter: HistoryFragmentAdapter by lazy {
        HistoryFragmentAdapter(this)
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<AppStatement> { renderData(it) })
        viewModel.getAllHistory()
        binding.historyFragmentRecyclerview.adapter = adapter
    }


    private fun renderData(appState: AppStatement) {
        with(binding) {
            when (appState) {
                is AppStatement.Error -> {
                    binding.root.showSnackBarWithoutAction(
                        getString(R.string.error_history_load_text),
                        Snackbar.LENGTH_SHORT
                    )
                }
                is AppStatement.Loading -> {
                }
                is AppStatement.Success -> {
                    adapter.setWeather(appState.weatherData)
                }
            }
        }
    }

    private fun View.showSnackBarWithoutAction(text: String, long: Int) {
        Snackbar.make(this, text, long).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onItemClick(weather: WeatherData) {

    }
}