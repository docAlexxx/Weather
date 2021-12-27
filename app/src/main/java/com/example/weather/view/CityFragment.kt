package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.viewmodel.AppStatement
import com.example.weather.viewmodel.MainViewModel


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
            is AppStatement.Error -> Toast.makeText(
                requireContext(),
                appState.error.message,
                Toast.LENGTH_SHORT
            ).show()
            is AppStatement.Loading -> Toast.makeText(
                requireContext(),
                "${appState.progress}",
                Toast.LENGTH_SHORT
            ).show()
            is AppStatement.Success -> {
                Toast.makeText(
                    requireContext(),
                    "${appState.temperature} (${appState.feelsLike})",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}