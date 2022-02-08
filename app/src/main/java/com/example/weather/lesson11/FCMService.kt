package com.example.weather.lesson11


import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService



    class FCMService : FirebaseMessagingService() {
        override fun onNewToken(s: String) {
            super.onNewToken(s)
            Log.d("mylogs", "token $s")
        }
    }
//token: eDJHY9Y8T0ubmJxWreafDO:APA91bHbWw-VqoTuJuFQRFYmx41s3e4bmFDheIb7QsQ7CJNKAn_CQ3Gd6TG2PGtlNOm-m7C4PByrfBGMzHgFPDhG2TZ6iIMisoSACtXWYQKM-Hwln4jsNk3Hjd3kYa60GmVTDIZoBs9E

