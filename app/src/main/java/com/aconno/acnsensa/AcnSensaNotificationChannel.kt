package com.aconno.acnsensa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * @author aconno
 */
class AcnSensaNotificationChannel(private val notificationManager: NotificationManager) {

    fun create() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "channel"
        private const val CHANNEL_NAME = "Default"
    }
}

fun createNotificationsChannel(application: Context) {
    val notificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val statsNotificationChannel = AcnSensaNotificationChannel(notificationManager)

    statsNotificationChannel.create()
}
