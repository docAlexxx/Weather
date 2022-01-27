package com.example.weather.view.list

import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.Utils.BUNDLE_KEY
import com.example.weather.Utils.KEY_SP
import com.example.weather.databinding.FragmentListBinding
import com.example.weather.model.WeatherData
import com.example.weather.view.city.CityFragment
import com.example.weather.viewmodel.AppStatement
import com.example.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class ListFragment : Fragment(), OnItemClick {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() {
            return _binding!!
        }

    private val adapter = ListFragmentAdapter(this)


    var isRussian by Delegates.notNull<Boolean>()

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<AppStatement> { showData(it) })
        val sp = getDefaultSharedPreferences(requireContext())
        isRussian = sp.getBoolean(KEY_SP, false)
        getCitiesList()
    }

    private fun initView() {
        with(binding) {
            listFragmentRecyclerView.adapter = adapter
            listFragmentFAB.setOnClickListener {
                changeRegion()
            }
        }
    }

    private fun getCitiesList() {
        with(binding.listFragmentFAB) {
            viewModel.apply {
                if (isRussian) {
                    getWeatherFromLocalSourceRus()
                    setImageResource(R.drawable.ic_russia)
                } else {
                    getWeatherFromLocalSourceWorld()
                    setImageResource(R.drawable.ic_earth)
                }
            }
        }
    }

    private fun changeRegion() {
        isRussian = !isRussian
        val sp = getDefaultSharedPreferences(requireContext())
        sp.edit().putBoolean(KEY_SP, isRussian).apply()
        getCitiesList()
    }

    private fun showData(appState: AppStatement) {
        binding.apply {
            with(listFragmentLoadingLayout) {
                when (appState) {
                    is AppStatement.Error -> {
                        visibility = View.GONE
                        binding.root.showSnackBarWithAction(getString(R.string.error_text),
                            getString(R.string.retry_text),
                            Snackbar.LENGTH_LONG,
                            { changeRegion() })
                    }
                    is AppStatement.Loading -> {
                        visibility = View.VISIBLE
                    }
                    is AppStatement.Success -> {
                        visibility = View.GONE
                        adapter.setWeather(appState.weatherData)
                        //   binding.root.showSnackBarWithoutAction(
                        //       getString(R.string.success_text),
                        //       Snackbar.LENGTH_SHORT
                        //   )
                    }
                }
            }
        }
    }

    private fun View.showSnackBarWithoutAction(text: String, long: Int) {
        Snackbar.make(this, text, long).show()
    }

    private fun View.showSnackBarWithAction(
        text: String,
        actionText: String,
        long: Int,
        block: () -> Unit
    ) {
        Snackbar.make(this, text, long).setAction(actionText) { block() }.show()
    }

    override fun onItemClick(weather: WeatherData) {
        activity?.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, CityFragment.newInstance(
                    Bundle().apply {
                        putParcelable(BUNDLE_KEY, weather)
                    }
                ))
                .addToBackStack("").commit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

}