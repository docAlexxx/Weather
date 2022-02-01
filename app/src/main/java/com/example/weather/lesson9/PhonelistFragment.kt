package com.example.weather.lesson9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    companion object {
        @JvmStatic
        fun newInstance() = PhonelistFragment()
    }

    fun getContacts(){

    }

}