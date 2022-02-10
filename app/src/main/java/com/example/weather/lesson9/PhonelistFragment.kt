package com.example.weather.lesson9

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.Utils.BindingFragment
import com.example.weather.Utils.REQUEST_CODE
import com.example.weather.databinding.FragmentCityBinding
import com.example.weather.databinding.FragmentPhonelistBinding


class PhonelistFragment : BindingFragment<FragmentPhonelistBinding>(FragmentPhonelistBinding::inflate) {

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

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ it->
        if(it){
            getContacts()
        }else{

        }
    }

    private fun myRequestPermission() {
      //  requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
        launcher.launch(Manifest.permission.READ_CONTACTS)
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

    private fun getContacts() {
        context?.let { it ->
            val contentResolver = it.contentResolver
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )


            cursor?.let { cursor ->
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))

                    val phone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC"
                    )

                    var number = ""
                    phone?.let { phone ->
                        for (j in 0 until phone.count) {
                            phone.moveToPosition(j)
                            val contactIdPhone =
                                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                            if (contactIdPhone == contactId) {
                                number =
                                    phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            }
                        }
                    }

                    addView(name, number)
                    phone?.close()
                }
            }
            cursor?.close()
        }
    }

    private fun addView(name: String, number: String) {
        binding.containerForContacts.addView(TextView(requireContext()).apply {
            text = name + ": " + number
            textSize = 30f
            setOnClickListener {
                onItemClick(number)
            }

        })
    }

    private fun onItemClick(number: String) {
        val intent = Intent(Intent.ACTION_DIAL);
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }
}