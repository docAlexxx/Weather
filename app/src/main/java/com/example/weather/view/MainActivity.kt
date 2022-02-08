package com.example.weather.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.lesson10.MapsFragment
import com.example.weather.lesson9.PhonelistFragment
import com.example.weather.view.history.HistoryFragment
import com.example.weather.view.list.ListFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance()).commit()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("mylogs_push", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("mylogs_push", " token $token")
            // Log and toast
            /*  val msg = getString(R.string.msg_token_fmt, token)

              Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()*/
        })

        pushNotification()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_history -> {
                val fragmentHistory = supportFragmentManager.findFragmentByTag("tagH")
                if (fragmentHistory == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, HistoryFragment.newInstance(), "tagH")
                        .addToBackStack("")
                        .commit()
                }
                true
            }
            R.id.menu_phonelist -> {
                val fragmentList = supportFragmentManager.findFragmentByTag("tagL")
                if (fragmentList == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, PhonelistFragment.newInstance(), "tagL")
                        .addToBackStack("").commit()
                }
                true
            }
            R.id.menu_google_maps -> {
                val fragmentMap = supportFragmentManager.findFragmentByTag("tagM")
                if (fragmentMap == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, MapsFragment(), "tagM").addToBackStack("")
                        .commit()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val NOTIFICATION_ID_2 = 2
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val CHANNEL_ID_2 = "channel_id_2"
    }

    private fun pushNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder_1 = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.weather_icon)
            setContentTitle("Заголовок для $CHANNEL_ID_1")
            setContentText("Сообщение для $CHANNEL_ID_1")
            priority = NotificationCompat.PRIORITY_MAX
        }
        val notificationBuilder_2 = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setSmallIcon(R.drawable.weather_icon)
            setContentTitle("Заголовок для $CHANNEL_ID_2")
            setContentText("Сообщение для $CHANNEL_ID_2")
            priority = NotificationCompat.PRIORITY_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName_2 = "Name $CHANNEL_ID_2"
            val channelDescription_2 = "Description for $CHANNEL_ID_2"
            val channelPriority_2 = NotificationManager.IMPORTANCE_LOW

            val channel_2 =
                NotificationChannel(CHANNEL_ID_2, channelName_2, channelPriority_2).apply {
                    description = channelDescription_2
                }
            notificationManager.createNotificationChannel(channel_2)
        }
        notificationManager.notify(NOTIFICATION_ID_2, notificationBuilder_2.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName_1 = "Name $CHANNEL_ID_1"
            val channelDescription_1 = "Description for $CHANNEL_ID_1"
            val channelPriority_1 = NotificationManager.IMPORTANCE_HIGH

            val channel_1 =
                NotificationChannel(CHANNEL_ID_1, channelName_1, channelPriority_1).apply {
                    description = channelDescription_1
                }
            notificationManager.createNotificationChannel(channel_1)
        }
        notificationManager.notify(NOTIFICATION_ID_1, notificationBuilder_1.build())

    }

}