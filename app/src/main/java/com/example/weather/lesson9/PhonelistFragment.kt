package com.example.weather.lesson9

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.Utils.REQUEST_CODE
import com.example.weather.databinding.FragmentPhonelistBinding

class PhonelistFragment : Fragment() {


    private var _binding: FragmentPhonelistBinding? = null
    private val binding: FragmentPhonelistBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhonelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }


    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Для корректной работы приложения необходим доступ к списку контактов")
            .setPositiveButton("Разрешить") { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton("Запретить") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
             if (requestCode == REQUEST_CODE) {

            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhonelistFragment()
    }

    fun getContacts(){

    }

}