package com.dscfuta.topnotch.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dscfuta.topnotch.MainActivity
import com.dscfuta.topnotch.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager
    private val CHANNEL_ID = "DSCFUTA"

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(TAG, "My Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let {
            Log.i(TAG, it.data["message"])

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //Setting up channel for android [0] and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                setupNotificationChannels()
            }

            val notificationId = Random().nextInt(60000)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_calls)
                    .setContentTitle(it.data["title"])
                    .setContentText(it.data["message"])
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannels(){
        val channelName = getString(R.string.fcm_name)
        val channelDescription = getString(R.string.fcm_description)

        val adminChannel : NotificationChannel
        adminChannel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW)
        adminChannel.description = channelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.CYAN
        adminChannel.enableVibration(true)

        notificationManager.createNotificationChannel(adminChannel)
    }
}