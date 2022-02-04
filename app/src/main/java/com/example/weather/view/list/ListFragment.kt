package com.example.weather.view.list

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.Utils.*
import com.example.weather.databinding.FragmentListBinding
import com.example.weather.model.City
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
            LocationFAB.setOnClickListener {
                checkPermission()
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
        showWeather(weather)
    }

    private fun showWeather(weather: WeatherData) {
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

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialog()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_message_no_gps))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {

            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialog()
                }
                else -> {
                    showDialog()
                }
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
    }

    private fun getLocation() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    providerGPS?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MIN_MOVING,
                            locationListener
                        )
                    }
                } else {
                    val lastLocation =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    lastLocation?.let {
                        getAddress(it)
                        Snackbar.make(
                            binding.root,
                            getString(R.string.dialog_title_gps_turned_off),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {

            }
        }
    }

    private fun getAddress(location: Location) {
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            requireActivity().runOnUiThread {
                showAddressDialog(listAddress[0].getAddressLine(0), location)
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_address_title))
            .setMessage(address)
            .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                showWeather(WeatherData(City(address, location.latitude, location.longitude)))
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

}