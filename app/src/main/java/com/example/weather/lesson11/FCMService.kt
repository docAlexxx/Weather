package com.example.weather.lesson11


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weather.R

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService : FirebaseMessagingService() {
        override fun onNewToken(s: String) {
            super.onNewToken(s)
            Log.d("mylogs", "token $s")
        }

        companion object {
            private const val NOTIFICATION_ID = 1
            private const val CHANNEL_ID = "channel_id"
            private const val KEY_TITLE = "myTitle"
            private const val KEY_MESSAGE = "myMessage"
        }
        private fun pushNotification(title:String,message:String) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID).apply {
                setSmallIcon(R.drawable.weather_icon)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_MAX
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelName = "Name $CHANNEL_ID"
                val channelDescription = "Description for $CHANNEL_ID"
                val channelPriority = NotificationManager.IMPORTANCE_HIGH

                val channel = NotificationChannel(CHANNEL_ID,channelName,channelPriority).apply {
                    description = channelDescription
                }
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
        }


        override fun onMessageReceived(message: RemoteMessage) {
            val data = message.data
            if(data.isNotEmpty()){
                val title =data[KEY_TITLE]
                val message =data[KEY_MESSAGE]
                if(!title.isNullOrBlank()&&!message.isNullOrBlank())
                    pushNotification(title,message)
            }
        }

    }
//token: eDJHY9Y8T0ubmJxWreafDO:APA91bHbWw-VqoTuJuFQRFYmx41s3e4bmFDheIb7QsQ7CJNKAn_CQ3Gd6TG2PGtlNOm-m7C4PByrfBGMzHgFPDhG2TZ6iIMisoSACtXWYQKM-Hwln4jsNk3Hjd3kYa60GmVTDIZoBs9E

