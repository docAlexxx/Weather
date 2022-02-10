package com.example.weather.lesson10

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.Utils.BUNDLE_KEY
import com.example.weather.Utils.BindingFragment
import com.example.weather.Utils.REQUEST_CODE_LOCATION
import com.example.weather.databinding.FragmentGoogleMapsMainBinding
import com.example.weather.model.City
import com.example.weather.model.WeatherData
import com.example.weather.view.city.CityFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsFragment : BindingFragment<FragmentGoogleMapsMainBinding>(FragmentGoogleMapsMainBinding::inflate) {

    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val startCity = LatLng(53.12, 50.06)
        googleMap.addMarker(MarkerOptions().position(startCity).title("Marker in Samara"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startCity))
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMapClickListener {
            getAddress(it)
        }

        googleMap.setOnMapLongClickListener {
            getAddress(it)
            showAddressDialog(it)
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

        } else {
            showDialog()
        }

    }

    private fun showAddressDialog(location: LatLng) {

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_address_get_weather))
            .setMessage(getString(R.string.dialog_address_get_weather_for_place))
            .setPositiveButton(getString(R.string.dialog_address_get_it)) { _, _ ->
                showWeather(
                    WeatherData(
                        City(
                            (binding.textAddress.text).toString(),
                            location.latitude,
                            location.longitude
                        )
                    )
                )
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
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

    private fun getAddress(location: LatLng) {

        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            requireActivity().runOnUiThread {
                binding.textAddress.text = listAddress[0].getAddressLine(0)
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.buttonSearch.setOnClickListener {
            searchPlace()
        }
    }

    private fun searchPlace() {
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocationName(binding.searchAddress.text.toString(), 1)
            if ( listAddress.size>0) {
                requireActivity().runOnUiThread {
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                listAddress[0].latitude,
                                listAddress[0].longitude
                            ), 15f
                        )
                    )
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                listAddress[0].latitude,
                                listAddress[0].longitude
                            )
                        ).title("")
                    )

                }

            } else {
                Snackbar.make(binding.mainMapView, "Can't find the place", Snackbar.LENGTH_SHORT).show()
            }
        }.start()
    }
}